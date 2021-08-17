package com.almaz.sarafanka.core.model

import android.os.Parcelable
import com.google.firebase.storage.StorageReference
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class User(
    val id: String,
    val phone: String,
    val name: String?,
    val photo: @RawValue StorageReference?
) : Parcelable
