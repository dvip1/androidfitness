package com.pantheons.gamifiedfitness.ui.common.exercisesession

import android.os.RemoteException
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.permission.HealthPermission.Companion.PERMISSION_READ_HEALTH_DATA_HISTORY
import androidx.health.connect.client.permission.HealthPermission.Companion.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.platform.client.permission.Permission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pantheons.gamifiedfitness.util.healthconnect.DailyStepsData
import com.pantheons.gamifiedfitness.util.healthconnect.HealthConnectManager
import com.pantheons.gamifiedfitness.util.healthconnect.WorkoutSessionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ExerciseSessionViewModel @Inject constructor(private val healthConnectManager: HealthConnectManager):
    ViewModel() {
    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class),
    )

    val backgroundReadPermissions = setOf(PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND)
    val historyReadPermissions = setOf(PERMISSION_READ_HEALTH_DATA_HISTORY)

    var permissionsGranted = mutableStateOf(false)
        private set

    var backgroundReadAvailable = mutableStateOf(false)
        private set

    var backgroundReadGranted = mutableStateOf(false)
        private set

    var historyReadAvailable = mutableStateOf(false)
        private set

    var historyReadGranted = mutableStateOf(false)
        private set

    var sessionsList: MutableState<List<ExerciseSessionRecord>> = mutableStateOf(listOf())
        private set

    var selectedWorkoutData: MutableState<WorkoutSessionData?> = mutableStateOf(null)
        private set

    var isLoadingWorkoutData: MutableState<Boolean> = mutableStateOf(false)
        private set

    private val _isLoadingTodaySteps: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isLoadingTodaySteps: StateFlow<Boolean> = _isLoadingTodaySteps
        private set

    var todayStepsData: MutableState<DailyStepsData?> = mutableStateOf(null)
        private set

    var uiState: UiState by mutableStateOf(UiState.Uninitialized)

    sealed class UiState {
        object Uninitialized : UiState()
        object Done : UiState()
        data class Error(val exception: Throwable, val uuid: UUID = UUID.randomUUID()) : UiState()
    }

    val permissionLauncher = healthConnectManager.requestPermissionsActivityContract()

    fun initialLoad() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                healthConnectManager.requestPermissionsActivityContract()
                readExerciseSessions()
            }
        }
    }

    private suspend fun readExerciseSessions() {
        val start = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(90)
        val now = Instant.now()

        healthConnectManager.readExerciseSessions(start.toInstant(), now)
            .onSuccess { exerciseSessions ->
                sessionsList.value = exerciseSessions
            }
            .onFailure { exception ->
                println("Error reading exercise sessions: ${exception.message}")
                uiState = UiState.Error(exception)
            }
    }

    suspend fun hasAllPermission(): Boolean{
        return healthConnectManager.hasAllPermissions(permissions)
    }
    private suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
        try {
            permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
            println("permissionsGranted.value: ${permissionsGranted.value}")
            backgroundReadGranted.value =
                healthConnectManager.hasAllPermissions(backgroundReadPermissions)
            historyReadGranted.value =
                healthConnectManager.hasAllPermissions(historyReadPermissions)
            backgroundReadAvailable.value = false
            historyReadAvailable.value = false
            if (permissionsGranted.value) {
                block()
            }
            uiState = UiState.Done
        } catch (remoteException: RemoteException) {
            uiState = UiState.Error(remoteException)
        } catch (securityException: SecurityException) {
            uiState = UiState.Error(securityException)
        } catch (ioException: IOException) {
            uiState = UiState.Error(ioException)
        } catch (illegalStateException: IllegalStateException) {
            uiState = UiState.Error(illegalStateException)
        }
    }
    fun loadSessionData(sessionId: String) {
        viewModelScope.launch {
            try {
                isLoadingWorkoutData.value = true

                healthConnectManager.readAssociatedSessionData(sessionId)
                    .onSuccess { data ->
                        selectedWorkoutData.value = data
                        uiState = UiState.Done
                    }
                    .onFailure { exception ->
                        println("Error reading session data: ${exception.message}")
                        uiState = UiState.Error(exception)
                    }
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            } finally {
                isLoadingWorkoutData.value = false
            }
        }
    }


    fun loadTodaySteps() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                _isLoadingTodaySteps.update {
                    true
                }

                try {
                    val today = LocalDate.now()
                    val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant()
                    val now = Instant.now()
                    val activeDuration = Duration.between(startOfDay, now)

                    // Read steps for today
                    readStepsForTimeRange(startOfDay, now)
                        .onSuccess { stepsCount ->
                            todayStepsData.value = DailyStepsData(
                                steps = stepsCount,
                                lastUpdated = now,
                                duration = activeDuration
                            )
                        }
                        .onFailure { exception ->
                            println("Error reading today's steps: ${exception.message}")
                            uiState = UiState.Error(exception)
                        }
                } finally {
                    _isLoadingTodaySteps.update {
                        false
                    }
                }
            }
        }

    }

    private suspend fun readStepsForTimeRange(
        start: Instant,
        end: Instant
    ): Result<Long> = runCatching {
        val aggregateRequest = AggregateRequest(
            metrics = setOf(StepsRecord.COUNT_TOTAL),
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )

        val response =healthConnectManager.healthConnectClient.aggregate(aggregateRequest)
        response[StepsRecord.COUNT_TOTAL] ?: 0L
    }

    // Add this to your HealthConnectManager class if not there already
    fun clearSelectedWorkoutData() {
        selectedWorkoutData.value = null
    }


}

class ExerciseSessionViewModelFactory(
    private val healthConnectManager: HealthConnectManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseSessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseSessionViewModel(
                healthConnectManager = healthConnectManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}