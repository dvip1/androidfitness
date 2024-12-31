package com.pantheons.gamifiedfitness.data.repository

import android.content.Context
import com.pantheons.gamifiedfitness.api.ApiService
import com.pantheons.gamifiedfitness.data.remote.model.ProfileResponse
import com.pantheons.gamifiedfitness.data.remote.model.ResponseData
import com.pantheons.gamifiedfitness.data.remote.model.UserAddRequest
import com.pantheons.gamifiedfitness.util.network.NetworkResult
import com.pantheons.gamifiedfitness.util.network.NetworkUtils
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) {
   suspend fun addUser(userAddRequest:UserAddRequest):NetworkResult<ResponseData> {
       return try {
              if(NetworkUtils.isNetworkAvailable(context)){
                val response = apiService.addUser(userAddRequest)
                if(response.isSuccessful){
                     NetworkResult.Success(response.body()!!)
                }else{
                     NetworkResult.Error("Server Error: ${response.code()}")
                }
              }else{
                NetworkResult.Error("Network Error")
              }
       }
       catch(e:Exception){
                NetworkResult.Error(NetworkUtils.handleApiError(e))
       }
   }
    suspend fun getUserProfile(uid:String):NetworkResult<ProfileResponse>
    {
        return try{
            if(NetworkUtils.isNetworkAvailable(context)){
                val response = apiService.getUserProfile(uid)
                if(response.isSuccessful){
                    NetworkResult.Success(response.body()!!)
                }
                else{
                    NetworkResult.Error("Server Error: ${response.code()}")
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