package com.almaz.sarafanka

import android.app.Application
import org.kodein.di.*

class SarafankaApp : Application(), KodeinAware {
    override lateinit var kodein: Kodein

    override val kodeinContext: KodeinContext<*>
        get() = AnyKodeinContext

    override val kodeinTrigger: KodeinTrigger?
        get() = KodeinTrigger()

    override fun onCreate() {
        super.onCreate()
        app = this
        kodein = com.almaz.sarafanka.di.Kodein().initKodein(this)
    }

    companion object {
        lateinit var app: SarafankaApp
    }
}
