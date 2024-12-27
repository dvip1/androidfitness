package com.pantheons.gamifiedfitness.util.auth

import com.pantheons.gamifiedfitness.data.repository.AuthRepositoryImpl
import javax.inject.Singleton

@Singleton
class AuthUtils (
    private val authRepository: AuthRepositoryImpl
) {
    suspend fun isAuthenticated(): Boolean {
        return authRepository.getCurrentUser() != null
    }
}