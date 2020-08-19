package com.almaz.sarafanka.presentation.main

import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.core.interactor.AuthInteractor
import com.almaz.sarafanka.presentation.base.BaseViewModel
import com.almaz.sarafanka.utils.states.LoadingState

class MainViewModel(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {
    override val loadingState = MutableLiveData(LoadingState.READY)
    val isLoggedIn = authInteractor.isLoggedInLiveData

    fun checkAuthUser() {
        authInteractor.isLoggedIn()
    }
}
