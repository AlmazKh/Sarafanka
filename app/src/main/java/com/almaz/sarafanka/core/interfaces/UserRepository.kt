package com.almaz.sarafanka.core.interfaces

interface UserRepository {
    suspend fun checkAuthUser(): Boolean
}