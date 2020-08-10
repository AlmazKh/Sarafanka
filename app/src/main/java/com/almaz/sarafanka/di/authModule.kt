package com.almaz.sarafanka.di

import android.content.Context
import com.google.firebase.auth.PhoneAuthProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun authModule() = Kodein.Module("authModule") {
    bind<PhoneAuthProvider>() with singleton { PhoneAuthProvider.getInstance() }
}
