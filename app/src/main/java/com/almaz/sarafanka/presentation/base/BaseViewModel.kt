package com.almaz.sarafanka.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.almaz.sarafanka.utils.LoadingState

abstract class BaseViewModel : ViewModel() {

    abstract val loadingState: MutableLiveData<LoadingState>
}
