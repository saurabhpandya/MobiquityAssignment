package com.mobiquityassignment.ui.favorite.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobiquityassignment.base.BaseViewModel
import com.mobiquityassignment.data.map.WeatherRepository
import com.mobiquityassignment.data.model.WeatherModel
import com.mobiquityassignment.ui.favorite.adapter.FavoriteAdapter
import com.mobiquityassignment.utility.MobiQuityPreference
import com.mobiquityassignment.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(
    application: Application,
    private val weatherRepository: WeatherRepository
) : BaseViewModel(application) {
    private val TAG = this::class.java.canonicalName

    var weatherDataResponse = MutableLiveData<Resource<WeatherModel>>()

    var favoriteCities: List<String> = ArrayList<String>()

    val favoriteAdapter = FavoriteAdapter(favoriteCities)

    fun getFavoroteCities(): List<String> {
        favoriteCities = MobiQuityPreference.weatherModelSet.toList()
        return favoriteCities
    }

    fun removeLocation(position: Int) {
        val weatherLocation = MobiQuityPreference.weatherModelSet.toMutableList()
        weatherLocation.remove(favoriteCities[position])
        MobiQuityPreference.weatherModelSet = weatherLocation.toSet()
        favoriteCities = MobiQuityPreference.weatherModelSet.toList()
        favoriteAdapter.setChat(favoriteCities)
        Log.d(TAG, "MobiQuityPreference.weatherModelSet:: ${MobiQuityPreference.weatherModelSet}")

    }

    fun getWeatherDataByCity(city: String) =
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                weatherDataResponse.value = Resource.loading(null)
            }
            try {
                var weatherModelResponse = WeatherModel()

                weatherModelResponse = weatherRepository.getWeatherDataByCity(city)

                withContext(Dispatchers.Main) {
                    Log.d(TAG, "weatherModelResponse: $weatherModelResponse")
                    weatherDataResponse.value = Resource.success(weatherModelResponse)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    weatherDataResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

}