package com.pantheons.gamifiedfitness.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthEvent
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthState
import com.pantheons.gamifiedfitness.util.auth.AuthUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authManager: AuthUtils
) : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    fun checkStatus(){
        viewModelScope.launch {
            authManager.checkAuthStatus()
        }
    }
    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> authManager.login(event.email, event.password)
            is AuthEvent.Register -> authManager.register(event.email, event.password, event.username)
            is AuthEvent.Logout -> authManager.logout()
        }
    }
}