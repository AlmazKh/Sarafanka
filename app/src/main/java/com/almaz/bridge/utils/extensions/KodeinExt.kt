package com.almaz.bridge.utils.extensions

import androidx.lifecycle.ViewModel
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Builder
import org.kodein.di.generic.bind

inline fun <reified T : ViewModel> Builder.bindViewModel(overrides: Boolean? = null): Kodein.Builder.TypeBinder<T> {
    return bind<T>(T::class.java.simpleName, overrides)
}
