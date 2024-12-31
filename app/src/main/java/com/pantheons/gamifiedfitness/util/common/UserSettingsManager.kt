package com.pantheons.gamifiedfitness.util.common

import android.content.Context

class UserSettingsManager(private val context: Context) {
    companion object {
        private val PREFERENCE_NAME = "user_settings"
        private val USER_DETAILS = "user_details"
    }

    fun saveUserPreferences(key:String, value:String, defaultValue:String="", name:String = PREFERENCE_NAME)
    {
        val sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }
    fun getUserPreferences(key:String, defaultValue:String="", name:String = PREFERENCE_NAME):String?
    {
        val sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE) ?: return null
        return sharedPref.getString(key, defaultValue)
    }
}