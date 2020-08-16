package com.almaz.sarafanka.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun appModule(context: Context) = Kodein.Module("appModule") {
    bind<Context>() with singleton { context }
    bind<FirebaseAuth>() with singleton { FirebaseAuth.getInstance() }
    bind<FirebaseFirestore>() with singleton { FirebaseFirestore.getInstance() }
    bind<FirebaseStorage>() with singleton { FirebaseStorage.getInstance() }
}
