package com.almaz.bridge.di

import android.app.Application
import org.kodein.di.Kodein

class Kodein {

    lateinit var di: Kodein

    fun initKodein(app: Application): Kodein {
        di = Kodein {
            import(appModule(app))
            import(viewModelModule())
            import(interactorModule())
            import(repoModule())
        }
        return di
    }
}
