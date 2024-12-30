package com.pantheons.gamifiedfitness.api

interface ApiEndpoint {
    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/"
        const val USER_ADD = "user/add"
        const val USERNAME_EXISTS = "user/exists/{username}" // Path parameter placeholder
        const val USER_PROFILE = "user/profile/{uid}"
    }
}