package com.almaz.sarafanka.utils.states

import androidx.lifecycle.MutableLiveData

enum class AuthState {
    CODE_SENDED,
    LOGGEDIN,
    REGISTERED,
    ERROR
}

fun MutableLiveData<AuthState>.codeSended() {
    this.value = AuthState.CODE_SENDED
}

fun MutableLiveData<AuthState>.loggedIn() {
    this.value = AuthState.LOGGEDIN
}

fun MutableLiveData<AuthState>.registered() {
    this.value = AuthState.REGISTERED
}

fun MutableLiveData<AuthState>.error() {
    this.value = AuthState.ERROR
}
