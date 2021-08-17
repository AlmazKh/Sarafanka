package com.almaz.sarafanka.presentation.service

import android.graphics.Bitmap
import android.util.Log
import com.almaz.sarafanka.core.interactor.ServiceInteractor
import com.almaz.sarafanka.presentation.base.BaseViewModel

class ServiceViewModel(private val serviceInteractor: ServiceInteractor) : BaseViewModel() {
    override val loadingState = serviceInteractor.loadingState
    val servicesLiveData = serviceInteractor.servicesLiveData
    val reviewPublishedLiveData = serviceInteractor.reviewPublishedLiveData

    init {
        getServices()
    }

    override fun refresh() {
        getServices()
    }

    fun getServices() {
        serviceInteractor.getServices(this.errorState)
    }

    fun publishReview(
        serviceId: String,
        recommended: Boolean,
        price: Int?,
        description: String?,
        images: List<Bitmap>?
    ) {
        serviceInteractor.publishReview(this, serviceId, recommended, price, description, images)
        Log.d(ServiceViewModel::class.java.simpleName, "Publishing data: $serviceId, $recommended, $price, $description, $images")
    }
}
