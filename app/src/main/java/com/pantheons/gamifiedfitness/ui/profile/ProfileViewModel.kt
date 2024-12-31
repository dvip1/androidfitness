package com.pantheons.gamifiedfitness.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantheons.gamifiedfitness.data.remote.model.ProfileResponse
import com.pantheons.gamifiedfitness.data.repository.UserRepositoryImpl
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthEvent
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthState
import com.pantheons.gamifiedfitness.util.auth.AuthUtils
import com.pantheons.gamifiedfitness.util.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val uid: String? = null,
    val username: String? = null,
    val email: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authManager: AuthUtils, private val userRepository: UserRepositoryImpl
) : ViewModel() {
    val profile: ProfileResponse = ProfileResponse("", "", "", 0, 0)
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    private val _profileState = MutableStateFlow<ProfileResponse?>(null)
    val profileState: StateFlow<ProfileResponse?> = _profileState

    fun checkStatus() {
        viewModelScope.launch {
            authManager.checkAuthStatus()
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> authManager.login(event.email, event.password)
            is AuthEvent.Register -> authManager.register(
                event.email, event.password, event.username
            )

            is AuthEvent.Logout -> authManager.logout()
        }
    }
    fun getUid(){
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            val uid = authManager.getUid()
            _authState.value = AuthState(isLoading = false, uid = uid)
        }
    }
    fun getUserProfile() {
        val uid = authState.value.uid
        viewModelScope.launch {

            val response = uid?.let { userRepository.getUserProfile(it) }
            when ( response ) {
                is NetworkResult.Success -> {
                    _uiState.value = ProfileState(isLoading = false, isAuthenticated = true)
                    _profileState.value = response.data
                }

                is NetworkResult.Error -> {
                    _uiState.value = ProfileState(isLoading = false, error = response.message)
                }

                is NetworkResult.Loading -> {
                    _uiState.value = ProfileState(isLoading = true)
                }

                null -> TODO()
            }
        }
    }
}