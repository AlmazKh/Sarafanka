package com.almaz.sarafanka.core.interfaces

import com.google.firebase.auth.AuthResult

interface UserRepository {
    suspend fun checkAuthUser(): Boolean
    suspend fun getVerificationCode(phone: String)
    suspend fun signInWithPhoneAuthCredential(verificationCode: String): AuthResult?
    suspend fun searchUserInDb(phone: String?)
}