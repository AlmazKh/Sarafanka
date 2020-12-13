package com.almaz.sarafanka.di

import com.almaz.sarafanka.core.interfaces.ServiceRepository
import com.almaz.sarafanka.core.interfaces.UserRepository
import com.almaz.sarafanka.data.repository.ServiceRepositoryImpl
import com.almaz.sarafanka.data.repository.UserRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun repoModule() = Kodein.Module("repoModule") {
    bind<UserRepository>() with singleton {
        UserRepositoryImpl(instance(), instance(), instance(), instance())
    }
    bind<ServiceRepository>() with singleton {
        ServiceRepositoryImpl(instance(), instance(), instance(), instance(), instance())
    }
}
