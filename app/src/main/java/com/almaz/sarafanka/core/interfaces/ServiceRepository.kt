package com.almaz.sarafanka.core.interfaces

import android.graphics.Bitmap
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.core.model.ServiceCategory
import com.almaz.sarafanka.utils.Response

interface ServiceRepository {
    suspend fun getServicesByUserId(id: String): List<Service>
    suspend fun getServices(): List<Service>
    suspend fun getServiceCategories(): List<ServiceCategory>
    suspend fun publishService(
        category: String,
        profession: String,
        description: String?,
        images: List<Bitmap>?
    ): Response<Boolean>

    suspend fun publishReview(
        serviceId: String,
        recommended: Boolean,
        price: Int?,
        description: String?,
        images: List<Bitmap>?
    ): Response<Boolean>
}
