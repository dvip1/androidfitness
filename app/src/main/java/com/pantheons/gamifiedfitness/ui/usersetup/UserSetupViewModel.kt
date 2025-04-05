package com.pantheons.gamifiedfitness.ui.usersetup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantheons.gamifiedfitness.util.common.UserSettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class UserSetupViewModel(private val userSettingsManager: UserSettingsManager) : ViewModel() {
    private val _page = mutableStateOf(1)
    private val _isSetupComplete = MutableStateFlow(false)
    val isSetupComplete = _isSetupComplete.asStateFlow()


    val page: Int
        get() = _page.value

    init {
        viewModelScope.launch {
            checkSetupCompletion()
        }
    }

    fun onNextClicked(vararg values: String) {
        when (page) {
            1 -> {
                // Store first page values
                viewModelScope.launch {
                    userSettingsManager.saveUserPreferences("name", values[0])
                    userSettingsManager.saveUserPreferences("age", values[1])
                }
                _page.value = 2
            }

            2 -> {
                // Store second page values and mark setup as complete
                viewModelScope.launch {
                    userSettingsManager.saveUserPreferences("start", values[0])
                    userSettingsManager.saveUserPreferences("goal", values[1])
                    userSettingsManager.saveUserPreferences("isFilled", "true")
                    _isSetupComplete.value = true
                }
            }
        }
    }

    private suspend fun checkSetupCompletion() {
        val isFilled = userSettingsManager.getUserPreferences("isFilled") == "true"
        _isSetupComplete.value = isFilled
    }

}

