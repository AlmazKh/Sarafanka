package com.almaz.sarafanka.core.interactor

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interfaces.ServiceRepository
import com.almaz.sarafanka.core.interfaces.UserRepository
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.presentation.base.InfoState
import com.almaz.sarafanka.utils.states.loading
import com.almaz.sarafanka.utils.states.ready
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServiceInteractor(
    private val serviceRepository: ServiceRepository,
    private val userRepository: UserRepository
) : BaseInteractor() {

    val servicesLiveData = MutableLiveData<List<Service>>()
    val reviewPublishedLiveData = MutableLiveData<Boolean>(false)

    fun getServices(errorState: MutableLiveData<String>) {
        launch {
            runCatching {
                loadingState.loading()
                withContext(Dispatchers.IO) {
                    serviceRepository.getServices()
                }
            }.onSuccess {
                loadingState.ready()
                servicesLiveData.postValue(
                    it.filter { service -> service.owner_id != userRepository.getCurrentUserId() }
                )
            }.onFailure {
                loadingState.ready()
                errorState.postValue(it.message)
            }
        }
    }

    fun publishReview(
        infoState: InfoState,
        serviceId: String,
        recommended: Boolean,
        price: Int?,
        description: String?,
        images: List<Bitmap>?
    ) {
        launch {
            val response = withContext(Dispatchers.IO) {
                serviceRepository.publishReview(serviceId, recommended, price, description, images)
            }
            if (response.error != null) {
                infoState.errorState.postValue("Не удалось опубликовать отзыв. Попробуйте снова")
            } else {
                reviewPublishedLiveData.postValue(true)
                infoState.successState.postValue("Отзыв успешно опубликован")
            }
        }
    }
}