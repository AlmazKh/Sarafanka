package com.almaz.sarafanka.data.repository

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import com.almaz.sarafanka.core.interfaces.UserRepository
import com.almaz.sarafanka.core.model.User
import com.almaz.sarafanka.utils.Response
import com.almaz.sarafanka.utils.extensions.getDownloadablePhotoUrl
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

private const val USER_ID = "user_id"
private const val USER_NAME = "name"
private const val USER_PHONE = "phone"
private const val USER_PHOTO = "photo"
private const val USERS = "users"

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val phoneAuthProvider: PhoneAuthProvider,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserRepository {

    private lateinit var storedVerificationId: String
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override suspend fun checkAuthUser(): Boolean = firebaseAuth.currentUser != null

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun getVerificationCode(phone: String) {
        phoneAuthProvider.verifyPhoneNumber(
            phone, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    Log.d(TAG, "onVerificationCompleted:$credential")

//                    signInWithPhoneAuthCredential(credential)

                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.w(TAG, "onVerificationFailed", e)

                    if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        // ...
                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // ...
                    }

                    // Show a message and update the UI
                    // ...
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d(TAG, "onCodeSent:$verificationId")

                    // Save verification ID and resending token so we can use them later
                    storedVerificationId = verificationId
                    resendToken = token

                    // ...
                }
            })
    }

    override suspend fun signInWithPhoneAuthCredential(verificationCode: String): AuthResult? {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, verificationCode)
        return try {
            firebaseAuth.signInWithCredential(credential).await()
        } catch (e: Exception) {
            null
        }

        /*.doOnComplete {
        searchUserInDb(phone, null)
            .observeOn(Schedulers.io())
            .subscribe({
                if (!it) {
                    addUserToDb(userName, null, phone)
                }
            }, {})
    }*/
    }

    override suspend fun searchUserInDb(phone: String?): Boolean {
        return try {
            val snapshot = db.collection(USERS).whereEqualTo(USER_PHONE, phone).get().await()
            !snapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addUserIntoDb(phone: String?, name: String?, photo: String?): Boolean {
        return try {
            val userMap = HashMap<String, Any?>()
            userMap[USER_ID] = firebaseAuth.currentUser?.uid
            userMap[USER_PHONE] = phone
            userMap[USER_NAME] = name
            userMap[USER_PHOTO] = photo
            db.collection(USERS)
                .document(firebaseAuth.currentUser?.uid.toString())
                .set(userMap)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateUserInfo(
        phone: String?,
        name: String?,
        photo: String?
    ): Response<Boolean> {
        return try {
            val userMap = HashMap<String, Any?>()
            if (phone != null) {
                userMap[USER_PHONE] = phone
            }
            if (name != null) {
                userMap[USER_NAME] = name
            }
            if (photo != null) {
                userMap[USER_PHOTO] = photo
            }
            db.collection(USERS)
                .document(firebaseAuth.currentUser?.uid.toString())
                .update(userMap)
                .await()
            return Response.success(true)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    override suspend fun loadAvatarIntoStorage(bitmap: Bitmap) {
        val avatarReference = "profile/${UUID.randomUUID()}"
        updateUserInfo(photo = avatarReference)
        val baos = ByteArrayOutputStream()
        if (bitmap.byteCount > 15000000)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        else
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        storage.reference.child(avatarReference).putBytes(baos.toByteArray())
    }

    override suspend fun getCurrentUser(): User {
        return try {
            val snapshot =
                db.collection(USERS)
                    .whereEqualTo(USER_ID, firebaseAuth.currentUser?.uid)
                    .get()
                    .await()
            mapDocumentToUser(snapshot.first())
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override suspend fun getUserById(userId: String): User {
        return try {
            val snapshot =
                db.collection(USERS)
                    .whereEqualTo(USER_ID, userId)
                    .get()
                    .await()
            mapDocumentToUser(snapshot.first())
        } catch (e: Exception) {
            throw e
        }
    }

    private fun mapDocumentToUser(documentSnapshot: QueryDocumentSnapshot): User =
        User(
            documentSnapshot.get(USER_ID).toString(),
            documentSnapshot.get(USER_PHONE).toString(),
            documentSnapshot.get(USER_NAME).toString(),
            storage.getDownloadablePhotoUrl(documentSnapshot.get(USER_PHOTO).toString())
        )
}
