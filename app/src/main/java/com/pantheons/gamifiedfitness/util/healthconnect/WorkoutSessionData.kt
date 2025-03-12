package com.pantheons.gamifiedfitness.util.healthconnect

import android.health.connect.datatypes.SpeedRecord
import android.health.connect.datatypes.units.Length
import android.health.connect.datatypes.units.Velocity
import java.time.Duration

data class WorkoutSessionData(
    val uid: String,
    val totalActiveSession: Duration? = null,
    val totalSteps: Long? = null,
    val totalDistance: Length? = null,
    val totalEnergyBurned: androidx.health.connect.client.units.Energy? = null,
    val minSpeed: Velocity? = null,
    val maxSpeed: Velocity? = null,
    val avgSpeed: Velocity? = null,
    val speedRecords: List<SpeedRecord>? = emptyList()  // Changed default from listOf() to emptyList()
)