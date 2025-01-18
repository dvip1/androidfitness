package com.pantheons.gamifiedfitness.api

interface ApiEndpoint {
    companion object {
        const val BASE_URL = "http://192.168.0.108:3000/"
        const val USER_ADD = "user/add"
        const val USERNAME_EXISTS = "user/exists/{username}" // Path parameter placeholder
        const val USER_PROFILE = "user/profile/{uid}"
        const val CREATE_COMMUNITY = "community/add"
        const val GET_USER_COMMUNITY = "community/user/{uid}"
        const val CREATE_POST = "post/add"
        const val GET_POSTS = "post/get/{community_id}/{type}"
    }
}