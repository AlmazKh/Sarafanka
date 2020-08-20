package com.almaz.sarafanka.core.interfaces

import com.almaz.sarafanka.core.model.Service

interface ServiceRepository {
    suspend fun getServicesByUserId(id: String): List<Service>
}
