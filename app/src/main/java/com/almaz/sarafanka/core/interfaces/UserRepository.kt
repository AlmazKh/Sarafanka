package com.almaz.sarafanka.core.interfaces

import android.net.Uri
import com.almaz.sarafanka.presentation.base.InfoState
import com.almaz.sarafanka.utils.Response
import com.google.firebase.auth.AuthResult

interface UserRepository {
    suspend fun checkAuthUser(): Boolean
    suspend fun getVerificationCode(phone: String)
    suspend fun signInWithPhoneAuthCredential(verificationCode: String): AuthResult?
    suspend fun searchUserInDb(phone: String?): Boolean
    suspend fun addUserIntoDb(phone: String?, name: String? = null, photo: String? = null): Boolean
    suspend fun updateUserInfo(phone: String? = null, name: String? = null, photo: String? = null): Response<Boolean>
    suspend fun loadAvatarIntoStorage(filePath: Uri)
}