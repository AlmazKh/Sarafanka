package com.almaz.sarafanka.core.interactor

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interfaces.UserRepository
import com.almaz.sarafanka.data.AuthManager
import com.almaz.sarafanka.presentation.base.InfoState
import com.almaz.sarafanka.utils.states.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthInteractor(
    private val userRepository: UserRepository,
    private val authManager: AuthManager
) : BaseInteractor() {

    private var storedPhoneNumber: String? = null

    val authState = MutableLiveData(AuthState.PHONE)

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
                infoState.errorState.postValue("Код не отправлен. Что-то пошло не так")
            }
        }
    }

    fun signInWithPhoneAuthCredential(infoState: InfoState, verificationCode: String) {
        launch {
            runCatching {
                loadingState.loadingFullscreen()
                withContext(Dispatchers.IO) {
                    userRepository.signInWithPhoneAuthCredential(verificationCode)
                }
            }.onSuccess {
                loadingState.ready()
                if (userRepository.searchUserInDb(storedPhoneNumber)) {
                    authState.loggedIn()
                    authManager.login()
                    infoState.successState.postValue("Авторизация прошла успешно")
                } else {
                    userRepository.addUserIntoDb(phone = it?.user?.phoneNumber)
                    authState.name()
                    infoState.successState.postValue("Аккаунт создан! Для завершения регистрации осталось пару шагов")
                }
            }.onFailure {
                loadingState.ready()
                infoState.errorState.postValue("Ошибка при создании аккаунта. Вы точно ввели корректный код?")
            }
        }
    }

    fun updateUserInfo(
        infoState: InfoState,
        phone: String? = null,
        name: String? = null,
        photo: String? = null
    ) {
        launch {
            val response = withContext(Dispatchers.IO) {
                userRepository.updateUserInfo(phone, name, photo)
            }
            if (response.error != null) {
                infoState.errorState.postValue("Что-то пошло не так")
            } else {
                authState.photo()
            }
        }
    }

    fun loadAvatarIntoStorage(infoState: InfoState, bitmap: Bitmap) {
        launch {
            runCatching {
                loadingState.loadingFullscreen()
                withContext(Dispatchers.IO) {
                    userRepository.loadAvatarIntoStorage(bitmap)
                }
            }.onSuccess {
                authState.registered()
                authManager.login()
                loadingState.ready()
                infoState.successState.postValue("Регистрация прошла успешно")
            }.onFailure {
                loadingState.ready()
                infoState.errorState.postValue("Ошибка. Попробуйте снова")
            }
        }
    }
}
