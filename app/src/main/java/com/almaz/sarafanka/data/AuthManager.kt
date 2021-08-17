package com.almaz.sarafanka.data

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.utils.Constants

class AuthManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
    private val prefsEditor: SharedPreferences.Editor = prefs.edit()

    val isLoggedIn = MutableLiveData<Boolean>(false)

    fun isLoggedIn(): Boolean {
        prefs.getBoolean(Constants.LOGGED, false).apply {
            takeIf { this != isLoggedIn.value }?.let {
                isLoggedIn.value = it
            }
        }
        return isLoggedIn.value == true
    }

    fun login() {
        prefsEditor
            .putBoolean(Constants.LOGGED, true)
            .commit()
        isLoggedIn.postValue(true)
    }

    fun logout() {
        prefsEditor.clear().commit()
        isLoggedIn.postValue(false)
    }
}
