package com.pantheons.gamifiedfitness.data.repository

import com.google.firebase.auth.FirebaseUser


interface AuthRepositoryImpl  {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String, username: String): Result<Unit>
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun logout(): Result<Unit>
}