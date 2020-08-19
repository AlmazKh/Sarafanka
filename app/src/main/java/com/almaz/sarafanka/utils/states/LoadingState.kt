package com.almaz.sarafanka.utils.states

import androidx.lifecycle.MutableLiveData

enum class LoadingState {
    READY, LOADING
}

fun MutableLiveData<LoadingState>.ready() {
    this.value = LoadingState.READY
}

fun MutableLiveData<LoadingState>.loading() {
    this.value = LoadingState.LOADING
}
