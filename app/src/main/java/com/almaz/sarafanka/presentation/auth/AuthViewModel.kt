package com.almaz.sarafanka.presentation.auth

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interactor.AuthInteractor
import com.almaz.sarafanka.presentation.base.BaseViewModel
import com.almaz.sarafanka.utils.states.LoadingState

class AuthViewModel(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {
    override val loadingState = authInteractor.loadingState
    val authState = authInteractor.authState

    fun getVerificationCode(phone: String) {
        authInteractor.getVerificationCode(this, phone)
    }

    fun verifySignInCode(verificationCode: String) {
        authInteractor.signInWithPhoneAuthCredential(this, verificationCode)
    }

    fun updateUserInfo(phone: String? = null, name: String? = null, photo: String? = null) {
        authInteractor.updateUserInfo(this, phone, name, photo)
    }

    fun loadAvatarIntoStorage(bitmap: Bitmap) {
        authInteractor.loadAvatarIntoStorage(this, bitmap)
    }
}
