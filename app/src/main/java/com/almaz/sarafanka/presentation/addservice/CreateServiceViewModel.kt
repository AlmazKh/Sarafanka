package com.almaz.sarafanka.presentation.addservice

import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interactor.ProfileInteractor
import com.almaz.sarafanka.presentation.base.BaseViewModel
import com.almaz.sarafanka.utils.states.LoadingState

class CreateServiceViewModel(private val profileInteractor: ProfileInteractor) : BaseViewModel() {
    override val loadingState = MutableLiveData<LoadingState>()
    val categoriesLiveData = profileInteractor.servicesCategoriesLiveData
    val servicePublishedLiveData = profileInteractor.servicePublishedLiveData

    init {
        getServiceCategories()
    }

    fun getServiceCategories() {
        profileInteractor.getServiceCategories()
    }

    fun publishService(category: String, profession: String, description: String?) {
        profileInteractor.publishService(this, category, profession, description)
    }
}