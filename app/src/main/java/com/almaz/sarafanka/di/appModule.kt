package com.almaz.sarafanka.di

import android.content.Context
import com.almaz.sarafanka.data.AuthManager
import com.almaz.sarafanka.data.ServiceCategoriesHolder
import com.google.firebase.auth.FirebaseAuth
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun appModule(context: Context) = Kodein.Module("appModule") {
    bind<Context>() with singleton { context }
//    bind<Application>() with singleton { SarafankaApp() }
    bind<FirebaseAuth>() with singleton { FirebaseAuth.getInstance() }
    bind<ServiceCategoriesHolder>() with singleton { ServiceCategoriesHolder() }
    bind<AuthManager>() with singleton { AuthManager(instance()) }
}
