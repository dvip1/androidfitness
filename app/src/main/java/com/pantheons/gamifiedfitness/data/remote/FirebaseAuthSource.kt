package com.pantheons.gamifiedfitness.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.pantheons.gamifiedfitness.data.repository.AuthRegisterResult
import com.pantheons.gamifiedfitness.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthSource @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    AuthRepositoryImpl {
    override suspend fun register(
        email: String,
        password: String,
        username: String
    ): Result<AuthRegisterResult> = try {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        Result.success(AuthRegisterResult.SuccessWithData(authResult.user!!.uid))
    } catch (e: Exception) {
        Result.success(AuthRegisterResult.Failure(e))
    }

    override suspend fun login(email: String, password: String): Result<AuthRegisterResult> = try {
        val authResult =  firebaseAuth.signInWithEmailAndPassword(email, password).await()
        Result.success(AuthRegisterResult.SuccessWithData(authResult.user!!.uid))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCurrentUser() = firebaseAuth.currentUser
    override suspend fun logout(): Result<Unit> = try {
        firebaseAuth.signOut()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}