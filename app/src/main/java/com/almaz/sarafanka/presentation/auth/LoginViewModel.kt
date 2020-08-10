package com.almaz.sarafanka.presentation.auth

import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.presentation.base.BaseViewModel
import com.almaz.sarafanka.utils.LoadingState
import com.almaz.sarafanka.utils.AuthState

class LoginViewModel : BaseViewModel() {
    override val loadingState = MutableLiveData(LoadingState.READY)
    val authState = MutableLiveData<AuthState>()



}
