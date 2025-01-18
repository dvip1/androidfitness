package com.pantheons.gamifiedfitness.api
import com.pantheons.gamifiedfitness.data.remote.model.CreateCommunityRequest
import com.pantheons.gamifiedfitness.data.remote.model.CreatePostRequest
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

    @POST(ApiEndpoint.CREATE_COMMUNITY)
    suspend fun createCommunity(@Body request: CreateCommunityRequest): Response<ResponseData>

   @POST(ApiEndpoint.CREATE_POST)
   suspend fun createPost(@Body request: CreatePostRequest): Response<ResponseData>

   @GET(ApiEndpoint.GET_USER_COMMUNITY)
   suspend fun getUserCommunity(@Path("uid")uid:String)
}