package com.pantheons.gamifiedfitness.util.healthconnect

import android.content.Context
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant

enum class HealthConnectAvailability {
    INSTALLED,
    NOT_INSTALLED,  // Changed from NOT_SUPPORTED for clarity
    NOT_SUPPORTED
}


const val MIN_SUPPORTED_SDK = Build.VERSION_CODES.O_MR1

class HealthConnectManager(private val context: Context) {
    val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }
    private val _availability = mutableStateOf(HealthConnectAvailability.NOT_SUPPORTED)
    val availability: State<HealthConnectAvailability> = _availability

    init {
        checkAvailability()
    }

    private fun checkAvailability() {
        _availability.value = when (HealthConnectClient.getSdkStatus(context)) {
            HealthConnectClient.SDK_AVAILABLE -> HealthConnectAvailability.INSTALLED
            HealthConnectClient.SDK_UNAVAILABLE -> HealthConnectAvailability.NOT_INSTALLED
            else -> HealthConnectAvailability.NOT_SUPPORTED
        }
    }

    suspend fun hasAllPermissions(permissions: Set<String>): Boolean {
        return try {
            healthConnectClient.permissionController.getGrantedPermissions()
                .containsAll(permissions)
        } catch (e: Exception) {
            false
        }
    }

    suspend fun aggregate(request: AggregateRequest): AggregationResult {
        return healthConnectClient.aggregate(request)
    }

    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }

    suspend fun readExerciseSessions(
        start: Instant,
        end: Instant
    ): Result<List<ExerciseSessionRecord>> = runCatching {
        val request = ReadRecordsRequest(
            recordType = ExerciseSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        healthConnectClient.readRecords(request).records
    }

    suspend fun readAssociatedSessionData(
        uid: String
    ): Result<WorkoutSessionData> = runCatching {
        val exerciseSession = healthConnectClient.readRecord(ExerciseSessionRecord::class, uid)

        val timeRangeFilter = TimeRangeFilter.between(
            startTime = exerciseSession.record.startTime,
            endTime = exerciseSession.record.endTime
        )

        val aggregateDataTypes = setOf(
            ExerciseSessionRecord.EXERCISE_DURATION_TOTAL,
            StepsRecord.COUNT_TOTAL,
            TotalCaloriesBurnedRecord.ENERGY_TOTAL,
        )

        val dataOriginFilter = setOf(exerciseSession.record.metadata.dataOrigin)

        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )

        val aggregateData = healthConnectClient.aggregate(aggregateRequest)

        WorkoutSessionData(
            uid = uid,
            totalActiveSession = aggregateData[ExerciseSessionRecord.EXERCISE_DURATION_TOTAL],
            totalSteps = aggregateData[StepsRecord.COUNT_TOTAL],
            totalEnergyBurned = aggregateData[TotalCaloriesBurnedRecord.ENERGY_TOTAL],
        )
    }
}