package com.almaz.sarafanka.utils.extensions

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

fun FirebaseStorage.getDownloadablePhotoUrl(url: String): StorageReference {
    return this.reference.child(url)
}