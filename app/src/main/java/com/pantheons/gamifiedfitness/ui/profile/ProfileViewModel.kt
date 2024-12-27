package com.pantheons.gamifiedfitness.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel: ViewModel() {
    private val _uiState = MutableStateFlow("Profile Screen Screen Data")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    fun updateData(newData: String) {
        _uiState.value = newData

    }
}