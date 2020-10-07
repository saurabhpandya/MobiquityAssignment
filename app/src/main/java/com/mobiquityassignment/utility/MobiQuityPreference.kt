package com.mobiquityassignment.utility

import android.content.Context
import android.content.SharedPreferences
import com.mobiquityassignment.data.model.WeatherModel


object MobiQuityPreference {
    private const val NAME = "MobiQuity"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // list of app specific preferences
    private val PREF_USER_NAME = Pair("pref_user_name", "")
    private val PREF_WEATHER_MODELS = Pair("pref_weather_models", HashSet<String>())

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var strWeatherModel: String
        get() = preferences.getString(PREF_USER_NAME.first, PREF_USER_NAME.second)!!
        set(value) = preferences.edit {
            it.putString(PREF_USER_NAME.first, value)
        }

    var weatherModelSet: Set<String>
        get() = preferences.getStringSet(
            PREF_WEATHER_MODELS.first,
            PREF_WEATHER_MODELS.second
        ) as Set<String>
        set(value) = preferences.edit {
            it.putStringSet(PREF_WEATHER_MODELS.first, value)
        }

    fun clear() {
        preferences.edit {
            it.clear()
        }
    }

    fun saveUserInfo(arylstWeather: ArrayList<WeatherModel>) {
        strWeatherModel = arylstWeather.toString()
    }

}
