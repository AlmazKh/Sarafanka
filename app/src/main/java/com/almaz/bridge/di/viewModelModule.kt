package com.almaz.bridge.di

import androidx.lifecycle.ViewModelProvider
import com.almaz.bridge.utils.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun viewModelModule() = Kodein.Module(name = "viewModelModule") {
    bind<ViewModelProvider.Factory>() with singleton {
        ViewModelFactory(
            kodein.direct
        )
    }
}
