package com.almaz.sarafanka.utils.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.almaz.sarafanka.presentation.base.BaseFragment

/*
fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}*/

fun <T> AppCompatActivity.observe(
    liveData: LiveData<T>,
    action: (t: T) -> Unit,
    refreshAction: (() -> Unit)? = null
) {
    liveData.removeObservers(this)
    liveData.observe(this, Observer {
        if (it == null) {
            if (this is BaseFragment<*>)
                refreshAction?.invoke()
        }
        it?.let { t -> action(t) }
    })
}

fun <T> Fragment.observe(
    liveData: LiveData<T>,
    action: (t: T) -> Unit,
    refreshAction: (() -> Unit)? = null
) {
    liveData.removeObservers(this)
    liveData.observe(viewLifecycleOwner, Observer {
        if (it == null) {
            if (this is BaseFragment<*>)
                refreshAction?.invoke()
        }
        it?.let { t -> action(t) }
    })
}
