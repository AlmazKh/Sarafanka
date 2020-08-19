package com.almaz.sarafanka.presentation.profile

import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interactor.ProfileInteractor
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.presentation.base.BaseViewModel
import com.almaz.sarafanka.utils.states.LoadingState

class ProfileViewModel(private val profileInteractor: ProfileInteractor) : BaseViewModel() {
    override val loadingState = MutableLiveData(LoadingState.READY)
    val profileInfoLiveData = profileInteractor.profileInfoLiveData
    val profileServicesLiveData = MutableLiveData<Service>()

    init {
        getProfileInfo()
        getProfileServices()
    }

    fun getProfileInfo() {
        profileInteractor.getProfileInfo(this.errorState)
    }

    fun getProfileServices() {

    }
}
