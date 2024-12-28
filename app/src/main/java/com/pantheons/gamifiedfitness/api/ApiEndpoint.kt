package com.pantheons.gamifiedfitness.api

interface ApiEndpoint {
    companion object {
        const val BASE_URL = "http://localhost:3000/"
        const val USER = "user"
        const val USER_ADD = "user/add"

        fun usernameExists(username: String): String {
            return "user/exists/$username"
        }

        fun userProfile(uid: String): String {
            return "user/profile/$uid"
        }
    }
}