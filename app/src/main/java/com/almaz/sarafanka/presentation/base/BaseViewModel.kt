package com.almaz.sarafanka.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.almaz.sarafanka.utils.InfoPanelManager
import com.almaz.sarafanka.utils.states.LoadingState

abstract class BaseViewModel : ViewModel(), InfoState {

    abstract val loadingState: MutableLiveData<LoadingState>

    override val errorState = InfoPanelManager.errorMainState
    override val successState = InfoPanelManager.successMainState

    open fun refresh() {}
}

interface InfoState {
    val successState: MutableLiveData<String>
    val errorState: MutableLiveData<String>
}
