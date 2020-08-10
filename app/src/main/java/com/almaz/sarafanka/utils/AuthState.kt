package com.almaz.sarafanka.utils

import androidx.lifecycle.MutableLiveData

enum class AuthState {
    LOGGEDIN,
    REGISTERED,
    ERROR
}

fun MutableLiveData<AuthState>.loggedin() {
    this.value = AuthState.LOGGEDIN
}

fun MutableLiveData<AuthState>.registered() {
    this.value = AuthState.REGISTERED
}

fun MutableLiveData<AuthState>.error() {
    this.value = AuthState.ERROR
}
