package com.example.tripso.domain.repository


import com.example.tripso.data.DataStateResult
import com.example.tripso.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    suspend fun signUp(
        userName: String,
        email: String,
        phone: String,
        password: String
    ): DataStateResult<Unit>


    suspend fun saveUserInDB(name: String, phone: String)

   suspend fun logIn(email: String, password: String): DataStateResult<Unit>

    suspend fun logout(): DataStateResult<Unit>

    suspend fun resetPassword(email: String): DataStateResult<Unit>

    suspend fun addTrip(trip: Trip?,reference:String)

    suspend fun getTrips(reference: String): Flow<DataStateResult<List<Trip>>>
}