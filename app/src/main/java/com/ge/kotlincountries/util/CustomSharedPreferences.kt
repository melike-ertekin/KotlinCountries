package com.ge.kotlincountries.util

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putLong
import androidx.core.content.edit
import androidx.preference.PreferenceManager


class CustomSharedPreferences {

    companion object{

        private val PREFERENCE_TIME = "preference_time"
        private var sharedPreference: SharedPreferences? = null

        @Volatile private  var instance: CustomSharedPreferences? = null

        private val lock = Any()

        operator  fun invoke(context: Context): CustomSharedPreferences = instance ?: synchronized(lock){
            instance ?: makeCustomSharedPreferences(context).also {
                instance = it
            }
        }

        private fun makeCustomSharedPreferences(context: Context): CustomSharedPreferences{
            sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }


}

    fun saveTime(time: Long){
        sharedPreference?.edit(commit = true){
            putLong(PREFERENCE_TIME, time)
        }
    }

    fun getTime() = sharedPreference?.getLong(PREFERENCE_TIME, 0)
}