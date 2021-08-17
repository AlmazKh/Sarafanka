package com.almaz.sarafanka.presentation.main

import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.presentation.base.BaseViewModel
import com.almaz.sarafanka.utils.states.LoadingState

class MainViewModel : BaseViewModel() {
    override val loadingState = MutableLiveData(LoadingState.READY)
}
