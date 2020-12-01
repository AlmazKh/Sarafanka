package com.almaz.sarafanka.presentation.service

import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interactor.ServiceInteractor
import com.almaz.sarafanka.presentation.base.BaseViewModel
import com.almaz.sarafanka.utils.states.LoadingState

class ServiceViewModel(private val serviceInteractor: ServiceInteractor) : BaseViewModel() {
    override val loadingState = MutableLiveData<LoadingState>()
    val servicesLiveData = serviceInteractor.servicesLiveData

    init {
        getServices()
    }

    fun getServices() {
        serviceInteractor.getServices(this.errorState)
    }
}
