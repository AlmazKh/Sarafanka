package com.almaz.sarafanka.core.interfaces

import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.core.model.ServiceCategory
import com.almaz.sarafanka.presentation.base.InfoState
import com.almaz.sarafanka.utils.Response

interface ServiceRepository {
    suspend fun getServicesByUserId(id: String): List<Service>
    suspend fun getServices(): List<Service>
    suspend fun getServiceCategories(): List<ServiceCategory>
    suspend fun publishService(category: String, profession: String, description: String?): Response<Boolean>
}
