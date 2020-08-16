package com.almaz.sarafanka.presentation.auth

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interactor.AuthInteractor
import com.almaz.sarafanka.presentation.base.BaseViewModel
import com.almaz.sarafanka.utils.LoadingState

class AuthViewModel(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {
    override val loadingState = MutableLiveData(LoadingState.READY)
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

    fun loadAvatarIntoStorage(filePath: Uri) {
        authInteractor.loadAvatarIntoStorage(this, filePath)
    }

    fun setIsLoggedInLiveDataTrue() {
        authInteractor.isLoggedInLiveData.postValue(true)
        successState.postValue("Success auth")
    }
}
