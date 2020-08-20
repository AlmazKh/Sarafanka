package com.almaz.sarafanka.core.model

import com.google.firebase.storage.StorageReference
import kotlinx.android.parcel.RawValue

data class Review(
    val id: String,
    val description: String,
    val isRecommended: Boolean,
    val inContacts: Boolean?,
    val photo: @RawValue List<StorageReference>,
    val service_price: Int?,
    val serviceId: String,
    val writerId: String
)
