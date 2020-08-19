package com.almaz.sarafanka.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Service(
    val id: String,
    val category: @RawValue ServiceCategory,
    val subcategory: String,
    val photo: String? = null,
    val description: String? = null,
    val reviews: @RawValue List<Review>? = null
) : Parcelable
