package com.almaz.sarafanka.di

import com.almaz.sarafanka.core.interactor.AuthInteractor
import com.almaz.sarafanka.core.interactor.ProfileInteractor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun interactorModule() = Kodein.Module("interactorModule") {
    bind<AuthInteractor>() with singleton {
        AuthInteractor(instance())
    }
    bind<ProfileInteractor>() with singleton {
        ProfileInteractor(instance(), instance())
    }
}
