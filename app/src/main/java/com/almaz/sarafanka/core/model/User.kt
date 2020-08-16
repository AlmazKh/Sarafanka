package com.almaz.sarafanka.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String,
    val phone: String,
    val name: String,
    val photo: String
) : Parcelable