package com.almaz.sarafanka.utils.states

import androidx.lifecycle.MutableLiveData

enum class AuthState {
    PHONE, // show phone layer
    CODE, // show code layer
    LOGGEDIN, // show MainActivity
    NAME, // show name layer
    PHOTO, // show photo layer
    REGISTERED, // show MainActivity
}

fun MutableLiveData<AuthState>.phone() {
    this.value = AuthState.PHONE
}

fun MutableLiveData<AuthState>.codeSended() {
    this.value = AuthState.CODE
}

fun MutableLiveData<AuthState>.loggedIn() {
    this.value = AuthState.LOGGEDIN
}

fun MutableLiveData<AuthState>.name() {
    this.value = AuthState.NAME
}

fun MutableLiveData<AuthState>.photo() {
    this.value = AuthState.PHOTO
}

fun MutableLiveData<AuthState>.registered() {
    this.value = AuthState.REGISTERED
}
