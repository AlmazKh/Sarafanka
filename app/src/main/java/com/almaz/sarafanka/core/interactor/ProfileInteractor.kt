package com.almaz.sarafanka.core.interactor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interfaces.ServiceRepository
import com.almaz.sarafanka.core.interfaces.UserRepository
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.core.model.ServiceCategory
import com.almaz.sarafanka.core.model.User
import com.almaz.sarafanka.data.AuthManager
import com.almaz.sarafanka.presentation.base.InfoState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileInteractor(
    private val userRepository: UserRepository,
    private val serviceRepository: ServiceRepository,
    private val authManager: AuthManager
) : BaseInteractor() {

    val profileInfoLiveData = MutableLiveData<User>()
    val profileServicesLiveData = MutableLiveData<List<Service>>()
    val servicesCategoriesLiveData = MutableLiveData<List<ServiceCategory>>()
    val servicePublishedLiveData = MutableLiveData<Boolean>(false)

    fun getProfileInfo(errorState: MutableLiveData<String>, userId: String? = null) {
        launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    if (userId != null) {
                        userRepository.getUserById(userId)
                    } else {
                        userRepository.getCurrentUserId()?.let {
                            userRepository.getUserById(it)
                        }
                    }
                }
            }.onSuccess {
                profileInfoLiveData.postValue(it)
            }.onFailure {
                errorState.postValue(it.message)
                Log.d("onFailure: ", it.message.toString())
            }
        }
    }

    fun getProfileServices(errorState: MutableLiveData<String>, userId: String? = null) {
        launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    if (userId != null) {
                        serviceRepository.getServicesByUserId(userId)
                    } else {
                        userRepository.getCurrentUserId()?.let {
                            serviceRepository.getServicesByUserId(it)
                        }
                    }
                }
            }.onSuccess {
                profileServicesLiveData.postValue(it)
            }.onFailure {
                errorState.postValue(it.message)
            }
        }
    }

    fun isCurrentUserProfile(userId: String): Boolean {
        var isCurrentUser = false
        launch {
            isCurrentUser = userId == userRepository.getCurrentUserId()
        }
        return isCurrentUser
    }

    fun logout() {
        launch {
            withContext(Dispatchers.IO) {
                userRepository.logout()
                authManager.logout()
            }
        }
    }

    fun getServiceCategories() {
        launch {
            withContext(Dispatchers.IO) {
                servicesCategoriesLiveData.postValue(
                    serviceRepository.getServiceCategories()
                )
            }
        }
    }

    fun publishService(infoState: InfoState, category: String, profession: String, description: String?) {
        launch {
            val response = withContext(Dispatchers.IO) {
                serviceRepository.publishService(category, profession, description)
            }
            if (response.error != null) {
                infoState.errorState.postValue("Не удалось опубликовать услугу. Попробуйте снова")
            } else {
                servicePublishedLiveData.postValue(true)
                infoState.successState.postValue("Услуга успешно опубликована")
            }
        }
    }
}
