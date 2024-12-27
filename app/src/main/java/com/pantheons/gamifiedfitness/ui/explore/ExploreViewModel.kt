package com.pantheons.gamifiedfitness.ui.explore

import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExploreViewModel:ViewModel()  {
    private val _uiState = MutableStateFlow("Explore Screen Data")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    fun updateData(newData: String) {
        _uiState.value = newData
    }
}
