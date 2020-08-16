package com.almaz.sarafanka.presentation.auth

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.AuthState
import com.almaz.sarafanka.utils.extensions.loadCircleImage
import com.almaz.sarafanka.utils.extensions.observe
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_upload_photo.*
import java.io.FileNotFoundException


class UploadPhotoFragment : BaseFragment<AuthViewModel>(AuthViewModel::class.java) {

    override val layoutId: Int = R.layout.fragment_upload_photo

    override fun setViewModel() {
        viewModel = ViewModelProvider(rootActivity, this.viewModelFactory)
            .get(AuthViewModel::class.java)
    }

    override fun setupView() {
        iv_upload_avatar.setOnClickListener {
            askPermission()
        }
        btn_approve_auth.setOnClickListener {
            rootActivity.navController.navigate(R.id.action_enterPhotoFragment_to_nav_graph)
            viewModel.setIsLoggedInLiveDataTrue()
        }
    }

   /* override fun subscribe(viewModel: AuthViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.authState, ::bindAuthState)
    }

    private fun bindAuthState(authState: AuthState) {
        when (authState) {
            AuthState.REGISTERED -> {
                rootActivity.navController.navigate(R.id.action_enterPhotoFragment_to_nav_graph)
            }
        }
    }*/

    private fun askPermission() {
        if (rootActivity.baseContext?.let {
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
            val snackbar = Snackbar.make(layout_upload_photo, message, Snackbar.LENGTH_LONG)
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
        startActivityForResult(photoPickerIntent, 10001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /* if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
             val result = CropImage.getActivityResult(data);
             if (resultCode == RESULT_OK) {
                 val resultUri = result.uri
                 iv_upload_avatar.loadCircleImage(resultUri.path!!)
                 viewModel.loadAvatarIntoStorage(resultUri.encodedPath.toString())
 //                getMultipartFromUri(resultUri)
             } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 val error = result.error
                 viewModel.errorState.value = error.message.toString()
             }
         } else*/
        if (resultCode == RESULT_OK) {
            try {
                val imageUri: Uri? = data?.data
                // Setting image on image view using Bitmap
                /* iv_upload_avatar.setImageBitmap(
                     MediaStore.Images.Media.getBitmap(
                         rootActivity.contentResolver,
                         imageUri
                     )
                 )*/
                iv_upload_avatar.loadCircleImage(imageUri.toString())
                imageUri?.let {
                    viewModel.loadAvatarIntoStorage(it)
                    btn_approve_auth.text = getString(R.string.next)
                }
                /*CropImage.activity(imageUri)
                    .setCropMenuCropButtonTitle("Сохранить")
                    .setFixAspectRatio(true)
                    .setAllowFlipping(true)
                    .setBackgroundColor(R.color.colorPrimary)
                    .setAllowRotation(false)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(rootActivity)*/
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }


}
