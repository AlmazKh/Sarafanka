package com.almaz.sarafanka.di

import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.presentation.addservice.CreateServiceViewModel
import com.almaz.sarafanka.presentation.auth.AuthViewModel
import com.almaz.sarafanka.presentation.contacts.ContactsViewModel
import com.almaz.sarafanka.presentation.main.MainViewModel
import com.almaz.sarafanka.presentation.profile.ProfileViewModel
import com.almaz.sarafanka.presentation.service.ServiceViewModel
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
        MainViewModel()
    }
    bindViewModel<AuthViewModel>() with provider {
        AuthViewModel(instance())
    }
    bindViewModel<ProfileViewModel>() with provider {
        ProfileViewModel(instance())
    }
    bindViewModel<ContactsViewModel>() with provider {
        ContactsViewModel()
    }
    bindViewModel<ServiceViewModel>() with provider {
        ServiceViewModel(instance())
    }
    bindViewModel<CreateServiceViewModel>() with provider {
        CreateServiceViewModel(instance())
    }
}
