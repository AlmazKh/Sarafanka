package com.almaz.sarafanka.core.interactor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interfaces.UserRepository
import com.almaz.sarafanka.presentation.base.InfoState
import com.almaz.sarafanka.utils.AuthState
import com.almaz.sarafanka.utils.codeSended
import com.almaz.sarafanka.utils.loggedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthInteractor(
    private val userRepository: UserRepository
) : BaseInteractor() {

    private var storedPhoneNumber: String? = null

    val isLoggedInLiveData = MutableLiveData<Boolean>()
    val authState = MutableLiveData<AuthState>()

    fun isLoggedIn() {
        launch {
            runCatching {
//                viewState.loading()
                withContext(Dispatchers.IO) {
                    userRepository.checkAuthUser()
                }
            }.onSuccess {
//                viewState.ready()
                isLoggedInLiveData.postValue(it)
            }.onFailure {
//                viewState.ready()
//                errorState.postValue(InfoPanelManager.getErrorInfo(it,action))
                Log.d("onFailure: ", it.message.toString())
            }
        }
    }

    fun getVerificationCode(infoState: InfoState, phone: String) {
        launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    userRepository.getVerificationCode(phone)
                }
            }.onSuccess {
                authState.codeSended()
                storedPhoneNumber = phone
                infoState.successState.postValue("Мы отправили вам код верификации")
            }.onFailure {
                infoState.errorState.postValue("Code doesn`t send. Smth went wrong")
            }
        }
    }

    fun signInWithPhoneAuthCredential(infoState: InfoState, verificationCode: String) {
        launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    userRepository.signInWithPhoneAuthCredential(verificationCode)
                }
            }.onSuccess {
                isLoggedInLiveData.postValue(true)
                authState.loggedIn()
                infoState.successState.postValue("Success auth!")
                userRepository.searchUserInDb(storedPhoneNumber)
            }.onFailure {
                infoState.errorState.postValue("Auth fail. Smth went wrong")
            }
        }
    }
}
