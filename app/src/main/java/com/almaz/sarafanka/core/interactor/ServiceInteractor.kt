package com.almaz.sarafanka.core.interactor

import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interfaces.ServiceRepository
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.utils.states.loading
import com.almaz.sarafanka.utils.states.ready
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
                loadingState.loading()
                withContext(Dispatchers.IO) {
                    serviceRepository.getServices()
                }
            }.onSuccess {
                loadingState.ready()
                servicesLiveData.postValue(it)
            }.onFailure {
                loadingState.ready()
                errorState.postValue(it.message)
            }
        }
    }
}