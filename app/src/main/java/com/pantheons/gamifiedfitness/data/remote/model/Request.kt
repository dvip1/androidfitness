package com.pantheons.gamifiedfitness.data.remote.model

data class UserAddRequest(
    val uid: String,
    val username: String,
    val email: String,
)

data class CreateCommunityRequest(
    val leader: String,
    val name: String,
    val description: String,
    val rules: String = "",
    val is_private: Boolean,
    val media: String = "",
    val members: List<String> = listOf()
)

data class CreatePostRequest(
    val uid: String,
    val community: String,
    val title: String,
    val tags: List<String>,
    val content: String,
    val is_template: Boolean,
    val media: String
)
