package com.pantheons.gamifiedfitness.util.healthconnect
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun dateTimeWithOffsetOrDefault(time: Instant, offset: ZoneOffset?): ZonedDateTime =
    if (offset != null) {
        ZonedDateTime.ofInstant(time, offset)
    } else {
        ZonedDateTime.ofInstant(time, ZoneId.systemDefault())
    }

fun Duration.formatTime() = String.format(
    java.util.Locale.getDefault(),
    "%02d:%02d:%02d",
    this.toHours() % 24,
    this.toMinutes() % 60,
    this.seconds % 60
)

fun Duration.formatHoursMinutes() = String.format(
    java.util.Locale.getDefault(),
    "%01dh%02dm",
    this.toHours() % 24,
    this.toMinutes() % 60
)
data class DailyStepsData(
    val steps: Long,
    val goal: Long = 10000,
    val lastUpdated: Instant = Instant.now(),
    val duration: Duration? = null
)