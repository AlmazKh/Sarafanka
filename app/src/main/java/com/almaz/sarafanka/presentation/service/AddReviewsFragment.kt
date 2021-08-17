package com.almaz.sarafanka.presentation.service

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.InfoPanelManager
import com.almaz.sarafanka.utils.LoadedImagesAdapter
import com.almaz.sarafanka.utils.extensions.hideKeyboard
import com.almaz.sarafanka.utils.extensions.observe
import com.almaz.sarafanka.utils.extensions.toGone
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_reviews.*
import java.io.FileNotFoundException

class AddReviewsFragment : BaseFragment<ServiceViewModel>(ServiceViewModel::class.java) {
    override val layoutId = R.layout.fragment_add_reviews

    private val loadedImagesAdapter = LoadedImagesAdapter {
        imagesUri = it
    }
    private var imagesUri = mutableListOf<Uri>()

    override fun setupView() {
        with(rv_loaded_images) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = loadedImagesAdapter
        }
        btn_back.setOnClickListener { rootActivity.navController.navigateUp() }
        btn_gallery.setOnClickListener {
            askPermission()
        }
        btn_publish.setOnClickListener {
            if (checkFields()) {
                viewModel.publishReview(
                    serviceId = arguments?.getString("service_id") ?: "",
                    recommended = switch_recommended.isChecked,
                    price = if (et_price.text.isNullOrBlank()) null else et_price.text.toString().toInt(),
                    description = et_description.text.toString(),
                    images = imagesUri.map {
                        MediaStore.Images.Media.getBitmap(
                            rootActivity.contentResolver, it
                        )
                    }
                )
            } else {
                InfoPanelManager.errorMainState.postValue("Необходимо заполнить все поля")
            }
        }
        et_description.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) v.hideKeyboard()
        }
    }

    override fun subscribe(viewModel: ServiceViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.reviewPublishedLiveData, ::bindPublished)
    }

    override fun onStart() {
        super.onStart()
        rootActivity.bottom_nav.toGone()
    }

    private fun bindPublished(status: Boolean) {
        if (status) {
            rootActivity.navController.navigateUp()
            viewModel.reviewPublishedLiveData.postValue(false)
        }
    }

    private fun checkFields(): Boolean {
        if (et_description.text.isNullOrBlank()) {
            til_description.error = "Поле не может быть пустым"
            return false
        }
        return true
    }

    private fun askPermission() {
        if (rootActivity.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE)
            } != PackageManager.PERMISSION_GRANTED) {
            requestPermissionWithRationale()
        } else {
            addPhoto()
        }
    }

    private fun requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                rootActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val message = "Разрешить доступ к файлам устройства?"
            val snackbar = Snackbar.make(layout_auth, message, Snackbar.LENGTH_LONG)
                .setTextColor(ContextCompat.getColor(rootActivity, R.color.colorTitle))
                .setBackgroundTint(ContextCompat.getColor(rootActivity, R.color.colorWhite))
                .setActionTextColor(ContextCompat.getColor(rootActivity, R.color.colorPrimary))
                .setAction("Да") { requestPerms() }
            val layout: Snackbar.SnackbarLayout = snackbar.view as Snackbar.SnackbarLayout
            layout.minimumHeight = (56 * Resources.getSystem().displayMetrics.density).toInt()
            snackbar.show()
        } else {
            requestPerms()
        }
    }

    private fun requestPerms() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(permissions, 123)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var allowed = false
        when (requestCode) {
            123 -> allowed =
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        if (allowed)
            addPhoto()
        else
            Toast.makeText(rootActivity, "Нет разрешения на доступ к хранилищу", Toast.LENGTH_LONG)
                .show()
    }

    private fun addPhoto() {
        val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, 10002)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            try {
                data?.data?.let { imagesUri.add(it) }
                loadedImagesAdapter.submitList(imagesUri)
                loadedImagesAdapter.notifyDataSetChanged()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}
