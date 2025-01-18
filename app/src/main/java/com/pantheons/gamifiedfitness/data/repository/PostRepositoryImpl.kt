package com.pantheons.gamifiedfitness.data.repository

import android.content.Context
import com.pantheons.gamifiedfitness.api.ApiService
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) {
}