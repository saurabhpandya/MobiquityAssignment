package com.mobiquityassignment.data.map

class WeatherNetworkDataProvider(private val weatherServices: WeatherServices) {
    private val TAG = this::class.java.canonicalName

    suspend fun getWeatherDataByCoOrdinates(lat: Double, lng: Double) =
        weatherServices.getWeatherDataByCoOrdinates(lat, lng)

    suspend fun getWeatherDataByCity(city: String) =
        weatherServices.getWeatherDataByCity(city)

}