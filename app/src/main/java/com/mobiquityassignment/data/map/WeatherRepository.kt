package com.mobiquityassignment.data.map

class WeatherRepository(private val weatherNetworkDataProvider: WeatherNetworkDataProvider) {

    suspend fun getWeatherDataByCoOrdinates(lat: Double, lng: Double) =
        weatherNetworkDataProvider.getWeatherDataByCoOrdinates(lat, lng)

    suspend fun getWeatherDataByCity(city: String) =
        weatherNetworkDataProvider.getWeatherDataByCity(city)
}