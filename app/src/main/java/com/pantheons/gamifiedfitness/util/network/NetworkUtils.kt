package com.pantheons.gamifiedfitness.util.network

import android.content.Context
import android.net.ConnectivityManager
import retrofit2.HttpException
import java.io.IOException

object NetworkUtils {

    fun isNetworkAvailable(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        return network != null
    }

    fun handleApiError(throwable:Throwable): String{
        return when(throwable){
            is IOException -> "Network Error"
            is HttpException -> "Server Error: ${throwable.code()}"
            else -> "Unknown Error"
        }
    }
}