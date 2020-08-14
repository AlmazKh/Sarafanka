package com.almaz.sarafanka.di

import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.presentation.auth.AuthViewModel
import com.almaz.sarafanka.presentation.main.MainViewModel
import com.almaz.sarafanka.utils.ViewModelFactory
import com.almaz.sarafanka.utils.extensions.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun viewModelModule() = Kodein.Module(name = "viewModelModule") {
    bind<ViewModelProvider.Factory>() with singleton {
        ViewModelFactory(
            kodein.direct
        )
    }
    bindViewModel<MainViewModel>() with provider {
        MainViewModel(instance())
    }
    bindViewModel<AuthViewModel>() with provider {
        AuthViewModel(instance())
    }
}
