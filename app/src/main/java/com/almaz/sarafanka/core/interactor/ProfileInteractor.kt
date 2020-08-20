package com.almaz.sarafanka.core.interactor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interfaces.ServiceRepository
import com.almaz.sarafanka.core.interfaces.UserRepository
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.core.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileInteractor(
    private val userRepository: UserRepository,
    private val serviceRepository: ServiceRepository
) : BaseInteractor() {

    val profileInfoLiveData = MutableLiveData<User>()
    val profileServicesLiveData = MutableLiveData<List<Service>>()

    fun getProfileInfo(errorState: MutableLiveData<String>) {
        launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    userRepository.getCurrentUser()
                }
            }.onSuccess {
                profileInfoLiveData.postValue(it)
            }.onFailure {
                errorState.postValue(it.message)
                Log.d("onFailure: ", it.message.toString())
            }
        }
    }

    fun getProfileServices(errorState: MutableLiveData<String>) {
        launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    userRepository.getCurrentUserId()?.let {
                        serviceRepository.getServicesByUserId(it)
                    }
                }
            }.onSuccess {
                profileServicesLiveData.postValue(it)
            }.onFailure {
                errorState.postValue(it.message)
            }
        }
    }
}