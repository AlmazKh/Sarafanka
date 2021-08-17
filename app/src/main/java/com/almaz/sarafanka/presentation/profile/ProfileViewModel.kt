package com.almaz.sarafanka.presentation.profile

import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interactor.ProfileInteractor
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.presentation.base.BaseViewModel
import com.almaz.sarafanka.utils.states.LoadingState

class ProfileViewModel(private val profileInteractor: ProfileInteractor) : BaseViewModel() {
    override val loadingState = MutableLiveData(LoadingState.READY)
    val profileInfoLiveData = profileInteractor.profileInfoLiveData
    val profileServicesLiveData = profileInteractor.profileServicesLiveData

    fun isCurrentUserProfile(userId: String): Boolean {
        return profileInteractor.isCurrentUserProfile(userId)
    }

    fun getProfileInfo(userId: String? = null) {
        profileInteractor.getProfileInfo(this.errorState, userId)
    }

    fun getProfileServices(userId: String? = null) {
        profileInteractor.getProfileServices(this.errorState, userId)
    }

    fun logout() {
        profileInteractor.logout()
    }
}
