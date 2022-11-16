package com.example.tripso.data.repository

import android.net.Uri
import android.util.Log
import com.example.tripso.R
import com.example.tripso.data.DataStateResult
import com.example.tripso.domain.model.User
import com.example.tripso.domain.repository.FirebaseRepository
import com.example.tripso.domain.util.Constants
import com.example.tripso.domain.util.DataStoreUtils
import com.example.tripso.domain.util.PrefsConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

const val TAG = "FirebaseRepositoryImpl"

@ExperimentalCoroutinesApi
class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
): FirebaseRepository {
    override suspend fun signUp(
        userName: String,
        email: String,
        phone: String,
        password: String,
    ): DataStateResult<Unit> {
        var result: DataStateResult<Unit> = DataStateResult.Loading()
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            saveUserInDB(
                userName,
                phone
            )
            result = DataStateResult.Success()
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> {
                    result = DataStateResult.Error(R.string.error_email_already_in_use)
                }
            }
            Log.d(TAG, "Error : ${e.errorCode}")
        } catch (e: Exception) {
            result = DataStateResult.Error(R.string.error_sign_up)
        }
        return result
    }

    private suspend fun saveUserInDB(
        name: String,
        phone: String
    ) {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let {
            val user = User(
                 userName = name, phone= phone
            )

            firebaseDatabase.getReference(Constants.REFERENCE_USERS)
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .setValue(user).await()

        }
    }

    //not used yet
    private suspend fun saveProfileImageInFirebaseStorage(profileImage: Uri): String {
        var profileImageUrl = ""
        try {
            val imageName = UUID.randomUUID().toString()
            profileImageUrl = Firebase.storage.reference.child("profile_images/$imageName")
                .putFile(profileImage).await().storage.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.d(TAG, "Error: saveProfileImageInFirebaseStorage(): ${e.message}")
        }
        return profileImageUrl
    }


    override suspend fun logIn(email: String, password: String): DataStateResult<Unit> {
        var result: DataStateResult<Unit> = DataStateResult.Loading()
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result = DataStateResult.Success()
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> {
                    result = DataStateResult.Error(R.string.error_user_not_found)
                }
                "ERROR_WRONG_PASSWORD" -> {
                    result = DataStateResult.Error(R.string.error_wrong_password)
                }
            }
            Log.d(TAG, "Error: ${e.errorCode}")
        } catch (e: Exception) {
            result = DataStateResult.Error(R.string.error_sign_in)
        }
        return result
    }

    override suspend fun resetPassword(email: String): DataStateResult<Unit> {
        var result: DataStateResult<Unit> = DataStateResult.Loading()
        try {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    result = DataStateResult.Success()
                }
            }
        } catch (e: Exception) {
            result = DataStateResult.Error(R.string.error_reset_password)
        }
        return result
    }

    override suspend fun logout(): DataStateResult<Unit> {
        val result: DataStateResult<Unit> = try {
            firebaseAuth.signOut()
            DataStateResult.Success()

        } catch (e: Exception) {
            DataStateResult.Error()
        }
        return result
    }


    //not used yet
    private suspend fun saveAllCurrentUserInformationLocally(currentUser: User?) {
        currentUser?.let { user ->
            DataStoreUtils.savePreference(
                key = PrefsConstants.MY_USER_NAME,
                value = user.userName
            )
            DataStoreUtils.savePreference(
                key = PrefsConstants.MY_USER_PHONE,
                value = user.phone
            )
        }
    }


}



