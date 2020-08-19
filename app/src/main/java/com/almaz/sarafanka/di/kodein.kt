package com.almaz.sarafanka.di

import android.app.Application
import org.kodein.di.Kodein

class Kodein {

    lateinit var di: Kodein

    fun initKodein(app: Application): Kodein {
        di = Kodein {
            import(appModule(app))
            import(databaseModule())
            import(interactorModule())
            import(repoModule())
            import(viewModelModule())
            import(authModule())
        }
        return di
    }
}
