package com.almaz.sarafanka.core.interactor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interfaces.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthInteractor(
    private val userRepository: UserRepository
) : BaseInteractor() {

    val authState = MutableLiveData<Boolean>()

    fun isLoggedIn() {
        launch {
            runCatching {
//                viewState.loading()
                withContext(Dispatchers.IO) {
                    userRepository.checkAuthUser()
                }
            }.onSuccess {
//                viewState.ready()
                authState.postValue(it)
            }.onFailure {
//                viewState.ready()
//                errorState.postValue(InfoPanelManager.getErrorInfo(it,action))
                Log.d("onFailure: ", it.message.toString())
            }
        }
    }
}