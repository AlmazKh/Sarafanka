package com.almaz.sarafanka.data.repository

import com.almaz.sarafanka.core.interfaces.UserRepository
import com.google.firebase.auth.FirebaseAuth

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : UserRepository {

    override suspend fun checkAuthUser(): Boolean = firebaseAuth.currentUser != null
}
