package com.pantheons.gamifiedfitness.data.repository

import android.content.Context
import com.pantheons.gamifiedfitness.api.ApiService
import com.pantheons.gamifiedfitness.data.remote.model.CreateCommunityRequest
import com.pantheons.gamifiedfitness.data.remote.model.ResponseData
import com.pantheons.gamifiedfitness.util.network.NetworkUtils
import com.pantheons.gamifiedfitness.util.network.NetworkResult
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) {
    suspend fun createCommunity(createCommunityRequest: CreateCommunityRequest): NetworkResult<ResponseData> {
        return try{
            if(NetworkUtils.isNetworkAvailable(context)){
                val response = apiService.createCommunity(createCommunityRequest)
                when{
                    response.isSuccessful -> NetworkResult.Success(response.body()!!)
                    else -> NetworkResult.Error("Server Error: ${response.code()}")
                }
            }
            else{
                NetworkResult.Error("Network Error")
            }
        }
        catch(e:Exception){
            NetworkResult.Error(NetworkUtils.handleApiError(e))
        }
    }

}