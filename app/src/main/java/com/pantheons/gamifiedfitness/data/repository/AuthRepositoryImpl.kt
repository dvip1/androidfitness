package com.pantheons.gamifiedfitness.data.repository

import com.google.firebase.auth.FirebaseUser


interface AuthRepositoryImpl  {
    suspend fun login(email: String, password: String): Result<AuthRegisterResult>
    suspend fun register(email: String, password: String, username: String): Result<AuthRegisterResult>
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun logout(): Result<Unit>
}
sealed class AuthRegisterResult {
    data object Success: AuthRegisterResult()
    data class SuccessWithData(val userId:String): AuthRegisterResult()
    data class Failure(val exception:Exception): AuthRegisterResult()
}