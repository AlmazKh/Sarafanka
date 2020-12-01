package com.almaz.sarafanka.core.interactor

import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interfaces.ServiceRepository
import com.almaz.sarafanka.core.model.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServiceInteractor(
    private val serviceRepository: ServiceRepository
) : BaseInteractor() {

    val servicesLiveData = MutableLiveData<List<Service>>()

    fun getServices(errorState: MutableLiveData<String>) {
        launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    serviceRepository.getServices()
                }
            }.onSuccess {
                servicesLiveData.postValue(it)
            }.onFailure {
                errorState.postValue(it.message)
            }
        }
    }
}