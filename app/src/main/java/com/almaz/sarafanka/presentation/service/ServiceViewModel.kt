package com.almaz.sarafanka.presentation.service

import com.almaz.sarafanka.core.interactor.ServiceInteractor
import com.almaz.sarafanka.presentation.base.BaseViewModel

class ServiceViewModel(private val serviceInteractor: ServiceInteractor) : BaseViewModel() {
    override val loadingState = serviceInteractor.loadingState
    val servicesLiveData = serviceInteractor.servicesLiveData

    init {
        getServices()
    }

    override fun refresh() {
        getServices()
    }

    fun getServices() {
        serviceInteractor.getServices(this.errorState)
    }
}
