package com.mobiquityassignment.data.map

import com.mobiquityassignment.data.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {

    @GET("?apikey=fae7190d7e6433ec3a45285ffcf55c86")
    suspend fun getWeatherDataByCoOrdinates(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double
    ): WeatherModel

    @GET("?apikey=fae7190d7e6433ec3a45285ffcf55c86")
    suspend fun getWeatherDataByCity(
        @Query("q") city: String
    ): WeatherModel

}