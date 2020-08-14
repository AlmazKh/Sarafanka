package com.almaz.sarafanka.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.almaz.sarafanka.core.interfaces.UserRepository
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit


class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val phoneAuthProvider: PhoneAuthProvider
) : UserRepository {

    private lateinit var storedVerificationId: String
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override suspend fun checkAuthUser(): Boolean = firebaseAuth.currentUser != null

    override suspend fun getVerificationCode(phone: String) {
        phoneAuthProvider.verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,
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

    override suspend fun searchUserInDb(phone: String?) {
        // TODO
    }

}

