package com.almaz.sarafanka.presentation.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseActivity
import com.almaz.sarafanka.presentation.main.MainActivity
import com.almaz.sarafanka.utils.extensions.loadCircleImage
import com.almaz.sarafanka.utils.extensions.observe
import com.almaz.sarafanka.utils.states.AuthState
import com.almaz.sarafanka.utils.states.codeSended
import com.almaz.sarafanka.utils.states.name
import com.almaz.sarafanka.utils.states.phone
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_auth.*
import java.io.FileNotFoundException

class AuthActivity : BaseActivity<AuthViewModel>(AuthViewModel::class.java) {
    override val layoutId = R.layout.activity_auth

    private var imageUri: Uri? = null

    override fun setupView() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        loading_fullscreen_view.setOnClickListener {}
        btn_next_to_verification.setOnClickListener {
            checkPhoneNumber("${tv_country_code.text}${et_phone_number.text}")
        }
        btn_approve_verification.setOnClickListener {
            if (et_verification_code.text.isNullOrBlank()) {
                viewModel.errorState.postValue("Введите код")
            } else {
                viewModel.verifySignInCode(et_verification_code.text.toString())
            }
        }
        btn_next_to_upload_photo.setOnClickListener {
            if (et_user_name.text.isNullOrBlank()) {
                viewModel.errorState.postValue("Введите ваше имя")
            } else {
                viewModel.updateUserInfo(name = et_user_name.text.toString())
            }
        }
        iv_upload_avatar.setOnClickListener {
            askPermission()
        }
        btn_approve_auth.setOnClickListener {
            imageUri?.let {
                viewModel.loadAvatarIntoStorage(
                    MediaStore.Images.Media.getBitmap(
                        contentResolver, it
                    )
                )
            }
        }
    }

    override fun subscribe(viewModel: AuthViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.authState, ::bindAuthState)
    }

    private fun bindAuthState(state: AuthState) {
        when (state) {
            AuthState.PHONE -> {
                enter_phone_layer.animate().translationX(0f)
                verification_code_layer.animate().translationX(1500f)
                name_layer.animate().translationX(1500f)
                photo_layer.animate().translationX(1500f)
            }
            AuthState.CODE -> {
                enter_phone_layer.animate().translationX(-1500f)
                verification_code_layer.animate().translationX(0f)
                name_layer.animate().translationX(1500f)
                photo_layer.animate().translationX(1500f)
            }
            AuthState.LOGGEDIN -> {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
            AuthState.NAME -> {
                enter_phone_layer.animate().translationX(-1500f)
                verification_code_layer.animate().translationX(-1500f)
                name_layer.animate().translationX(0f)
                photo_layer.animate().translationX(1500f)
            }
            AuthState.PHOTO -> {
                enter_phone_layer.animate().translationX(-1500f)
                verification_code_layer.animate().translationX(-1500f)
                name_layer.animate().translationX(-1500f)
                photo_layer.animate().translationX(0f)
            }
            AuthState.REGISTERED -> {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        when (viewModel.authState.value) {
            AuthState.PHONE -> {
                super.onBackPressed()
            }
            AuthState.CODE -> {
                viewModel.authState.phone()
            }
            AuthState.LOGGEDIN -> {
                super.onBackPressed()
            }
            AuthState.NAME -> {
                viewModel.authState.codeSended()
            }
            AuthState.PHOTO -> {
                viewModel.authState.name()
            }
            AuthState.REGISTERED -> {
                super.onBackPressed()
            }
        }
    }

    private fun checkPhoneNumber(phone: String) {
        if (phone.length < 12) {
            viewModel.errorState.postValue("Введите корректный номер телефона")
        } else {
            viewModel.getVerificationCode(phone)
        }
    }

    private fun askPermission() {
        if (baseContext?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE)
            } != PackageManager.PERMISSION_GRANTED) {
            requestPermissionWithRationale()
        } else {
            addPhoto()
        }
    }

    private fun requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val message = "Разрешить доступ к файлам устройства?"
            val snackbar = Snackbar.make(layout_auth, message, Snackbar.LENGTH_LONG)
                .setTextColor(ContextCompat.getColor(this, R.color.colorTitle))
                .setBackgroundTint(ContextCompat.getColor(this, R.color.colorWhite))
                .setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
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
            Toast.makeText(this, "Нет разрешения на доступ к хранилищу", Toast.LENGTH_LONG)
                .show()
    }

    private fun addPhoto() {
        val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, 10001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data?.data
                iv_upload_avatar.loadCircleImage(imageUri.toString())
                btn_approve_auth.text = getString(R.string.next)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}