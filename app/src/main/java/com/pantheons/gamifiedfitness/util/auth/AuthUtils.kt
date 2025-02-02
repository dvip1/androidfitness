package com.pantheons.gamifiedfitness.util.auth

import android.util.Log
import com.pantheons.gamifiedfitness.data.remote.model.UserAddRequest
import com.pantheons.gamifiedfitness.data.repository.AuthRegisterResult
import com.pantheons.gamifiedfitness.data.repository.AuthRepositoryImpl
import com.pantheons.gamifiedfitness.data.repository.UserRepositoryImpl
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUtils @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    private val userRepository: UserRepositoryImpl
) {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    suspend fun checkAuthStatus() {
        val currentUser = authRepository.getCurrentUser()
        _authState.value = _authState.value.copy(
            isAuthenticated = currentUser != null
        )
    }

    fun login(email: String, password: String) {
        managerScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            authRepository.login(email, password)
                .onSuccess { authResult ->
                    val uid = (authResult as AuthRegisterResult.SuccessWithData).userId
                    Log.d("AuthUtils", "login: success")
                    checkAuthStatus() // Verify authentication state after login
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        uid = uid,
                    )
                    Log.d("AuthUtils", "login: ${_authState.value}")
                }
                .onFailure { exception ->
                    Log.d("AuthUtils", "login: ${exception.message}")
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
        }
    }

    fun register(email: String, password: String, username: String) {
        managerScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
           authRepository.register(email, password, username)
               .onSuccess { authResult ->
                   val uid = (authResult as AuthRegisterResult.SuccessWithData).userId
                   userRepository.addUser(UserAddRequest(uid, username, email))
                   checkAuthStatus() // Verify authentication state after registration
                   _authState.value = _authState.value.copy(
                       isLoading = false,
                       isAuthenticated = true,
                       uid = uid,
                       username = username,
                   )

               }
               .onFailure { exception ->
                   _authState.value = _authState.value.copy(
                       isLoading = false,
                       error = exception.message
                   )
               }
        }
    }

    fun logout() {
        managerScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            authRepository.logout()
                .onSuccess {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        isAuthenticated = false
                    )
                }
                .onFailure { exception ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
        }
    }
    suspend fun getUid(): String{
        return authRepository.getCurrentUser()?.uid ?: ""
    }
    suspend fun isAuthenticated(): Boolean {
        return authRepository.getCurrentUser() != null
    }
}