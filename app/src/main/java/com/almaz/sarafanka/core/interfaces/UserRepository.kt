package com.almaz.sarafanka.core.interfaces

import android.graphics.Bitmap
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.core.model.User
import com.almaz.sarafanka.utils.Response
import com.google.firebase.auth.AuthResult

interface UserRepository {
    suspend fun checkAuthUser(): Boolean
    suspend fun getVerificationCode(phone: String)
    suspend fun signInWithPhoneAuthCredential(verificationCode: String): AuthResult?
    suspend fun searchUserInDb(phone: String?): Boolean
    suspend fun addUserIntoDb(phone: String?, name: String? = null, photo: String? = null): Boolean
    suspend fun updateUserInfo(phone: String? = null, name: String? = null, photo: String? = null): Response<Boolean>
    suspend fun loadAvatarIntoStorage(bitmap: Bitmap)
    suspend fun getCurrentUser(): User
    suspend fun getCurrentUserId(): String?
    suspend fun getUserById(userId: String): User
    suspend fun logout()
}
