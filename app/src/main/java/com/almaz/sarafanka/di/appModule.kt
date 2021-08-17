package com.almaz.sarafanka.di

import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun appModule(context: Context) = Kodein.Module("appModule") {
    bind<Context>() with singleton { context }
}
