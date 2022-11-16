package com.example.tripso.domain.repository


import com.example.tripso.data.DataStateResult

interface FirebaseRepository {

    suspend fun signUp(
        userName: String,
        email: String,
        phone: String,
        password: String
    ): DataStateResult<Unit>


   suspend fun logIn(email: String, password: String): DataStateResult<Unit>

    suspend fun logout(): DataStateResult<Unit>

    suspend fun resetPassword(email: String): DataStateResult<Unit>


}