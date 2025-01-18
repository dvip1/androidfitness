package com.pantheons.gamifiedfitness.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantheons.gamifiedfitness.util.auth.AuthUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val uid: String? = null,
    val username: String? = null,
    val email: String? = null
)

sealed class AuthEvent {
    data class Login(val email: String, val password: String) : AuthEvent()
    data class Register(val email: String, val password: String, val username: String) : AuthEvent()
    data object Logout : AuthEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthUtils,
) : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun checkStatus() {
        viewModelScope.launch {
            authManager.checkAuthStatus()
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> authManager.login(event.email, event.password)
            is AuthEvent.Register -> authManager.register(
                event.email,
                event.password,
                event.username
            )

            is AuthEvent.Logout -> authManager.logout()
        }
    }
    fun confirmPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}