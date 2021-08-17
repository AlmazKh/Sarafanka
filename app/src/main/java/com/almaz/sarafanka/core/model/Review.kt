package com.almaz.sarafanka.core.model

import android.os.Parcelable
import com.google.firebase.storage.StorageReference
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Review(
    val id: String,
    val description: String,
    val isRecommended: Boolean,
    val inContacts: Boolean?,
    val photo: @RawValue List<String>? = null,
    val service_price: Int?,
    val serviceId: String,
    val writer: User
) : Parcelable
