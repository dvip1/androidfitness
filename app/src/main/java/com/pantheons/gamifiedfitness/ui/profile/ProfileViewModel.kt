package com.pantheons.gamifiedfitness.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantheons.gamifiedfitness.data.remote.model.ProfileResponse
import com.pantheons.gamifiedfitness.data.remote.model.ResponseData
import com.pantheons.gamifiedfitness.data.remote.model.UserAddRequest
import com.pantheons.gamifiedfitness.data.repository.AuthRepositoryImpl
import com.pantheons.gamifiedfitness.data.repository.UserRepositoryImpl
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthEvent
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthState
import com.pantheons.gamifiedfitness.util.auth.AuthUtils
import com.pantheons.gamifiedfitness.util.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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