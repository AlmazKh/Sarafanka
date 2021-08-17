package com.almaz.sarafanka.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceCategory(
    val id: Int,
    val name: String,
    val professions: List<String>
) : Parcelable
