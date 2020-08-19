package com.almaz.sarafanka.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun databaseModule() = Kodein.Module("databaseModule") {
    bind<FirebaseFirestore>() with singleton { FirebaseFirestore.getInstance() }
    bind<FirebaseStorage>() with singleton { FirebaseStorage.getInstance() }
}
