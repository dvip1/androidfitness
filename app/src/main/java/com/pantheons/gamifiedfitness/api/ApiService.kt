package com.pantheons.gamifiedfitness.api
import com.pantheons.gamifiedfitness.data.remote.model.ProfileResponse
import com.pantheons.gamifiedfitness.data.remote.model.ResponseData
import com.pantheons.gamifiedfitness.data.remote.model.UserAddRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService{
    @POST(ApiEndpoint.USER_ADD)
    suspend fun addUser(@Body request: UserAddRequest): Response<ResponseData>

    @GET(ApiEndpoint.USERNAME_EXISTS)
    suspend fun usernameExists(@Path("username")username: String): Response<ResponseData>

    @GET(ApiEndpoint.USER_PROFILE)
    suspend fun getUserProfile(@Path("uid")uid: String): Response<ProfileResponse>
}