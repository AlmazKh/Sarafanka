package com.almaz.sarafanka.utils

import android.os.Handler
import androidx.lifecycle.MutableLiveData

class TemporaryLiveData(string: String) : MutableLiveData<String>(string) {
    private val handler = Handler()
    private var isShowing = false

    override fun postValue(value: String?) {
        if (isShowing)
            handler.removeCallbacksAndMessages(null)
        if (this.value?.isNotEmpty() == true) {
            super.postValue("")
        }
        handler.postDelayed({ super.postValue(value) }, 150)
        handler.postDelayed({
            if (this.value == value) {
                super.postValue("")
                isShowing = false
            }
        }, 3000)
        isShowing = true
    }
}