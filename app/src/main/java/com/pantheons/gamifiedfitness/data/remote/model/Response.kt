package com.pantheons.gamifiedfitness.data.remote.model

data class ProfileResponse (
    val uid: String,
    val username: String,
    val profileImage: String,
    val streaks: Number,
    val karmas: Number,
)
data class ResponseData (
    val message: String
)
