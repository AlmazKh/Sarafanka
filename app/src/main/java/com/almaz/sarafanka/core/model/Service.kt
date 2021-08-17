package com.almaz.sarafanka.core.model

import android.os.Parcelable
import com.google.firebase.storage.StorageReference
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Service(
    val id: String?,
    val category: @RawValue ServiceCategory,
    val subcategory: String,
    val photo: @RawValue List<StorageReference>? = null,
    val description: String? = null,
    val reviews: @RawValue List<Review>? = null,
    val owner_id: String
) : Parcelable
