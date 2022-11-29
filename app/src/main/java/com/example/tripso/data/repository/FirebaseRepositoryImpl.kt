package com.example.tripso.data.repository

import android.net.Uri
import android.util.Log
import com.example.tripso.R
import com.example.tripso.data.DataStateResult
import com.example.tripso.domain.model.Trip
import com.example.tripso.domain.model.User
import com.example.tripso.domain.repository.FirebaseRepository
import com.example.tripso.domain.util.Constants
import com.example.tripso.domain.util.DataStoreUtils
import com.example.tripso.domain.util.PrefsConstants
import com.example.tripso.domain.util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
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

    override suspend fun saveUserInDB(
        name: String,
        phone: String
    ) {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let {
            val user = User(
                 userName = name, phone= phone
            )

            firebaseDatabase.getReference(Constants.REFERENCE_USERS)
                .child(Utils.getCurrentUserId())
                .setValue(user).await()
        }

        saveAllCurrentUserInformationLocally(User(userName = name,phone = phone))
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


    private suspend fun saveAllCurrentUserInformationLocally(currentUser: User?) {
        currentUser?.let { user ->
            DataStoreUtils.savePreference(
                key = PrefsConstants.MY_USER_NAME,
                value = user.userName
            )
            DataStoreUtils.savePreference(
                key = PrefsConstants.MY_USER_EMAIL,
                value = FirebaseAuth.getInstance().currentUser!!.email.toString()
            )
            DataStoreUtils.savePreference(
                key = PrefsConstants.MY_USER_PHONE,
                value = user.phone
            )
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

    override suspend fun addTrip(trip: Trip?, reference:String) {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let {
            firebaseDatabase.getReference(reference)
                .child(Utils.getCurrentUserId())
                .child(trip?.tripName + trip?.tripFrom +trip?.tripTo)
                .setValue(trip).await()
        }

    }

    override suspend fun getTrips(reference: String) = callbackFlow<DataStateResult<List<Trip>>> {
        val trips = mutableListOf<Trip>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.map { ds ->
                    val result = ds.getValue(Trip::class.java)
                    result?.startDate = Utils.convertLongToTime(result?.startDate?.toLong()!!)
                    result.endDate = Utils.convertLongToTime(result.endDate?.toLong()!!)
                    trips.add(result)
                }
                this@callbackFlow.trySendBlocking(DataStateResult.Success(data = trips.toList()))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Ahmed","DatabaseError: ${error.message}")
                this@callbackFlow.trySendBlocking(DataStateResult.Error(errorMessageResId = error.code))
            }
        }
        firebaseDatabase.getReference(reference)
            .child(Utils.getCurrentUserId())
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase.getReference(reference)
                .child(Utils.getCurrentUserId())
                .removeEventListener(postListener)
        }
    }
}



