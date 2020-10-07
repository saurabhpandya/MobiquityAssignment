package com.mobiquityassignment.ui.map.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobiquityassignment.base.BaseViewModel
import com.mobiquityassignment.data.map.WeatherRepository
import com.mobiquityassignment.data.model.WeatherModel
import com.mobiquityassignment.utility.MobiQuityPreference
import com.mobiquityassignment.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(
    application: Application,
    val weatherRepository: WeatherRepository
) : BaseViewModel(application) {
    private val TAG = this::class.java.canonicalName

    var weatherDataResponse = MutableLiveData<Resource<WeatherModel>>()

    fun getWeatherDataByCoOrdinates(lat: Double, lng: Double) =
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                weatherDataResponse.value = Resource.loading(null)
            }
            try {
                var weatherModelResponse = WeatherModel()

                weatherModelResponse = weatherRepository.getWeatherDataByCoOrdinates(lat, lng)

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

    fun saveLocation() {
        val weatherModel = weatherDataResponse.value?.data
        val weatherLocation = MobiQuityPreference.weatherModelSet.toMutableList()
        weatherLocation.add(weatherModel?.name!!)
        MobiQuityPreference.weatherModelSet = weatherLocation.toSet()
        Log.d(TAG, "MobiQuityPreference.weatherModelSet:: ${MobiQuityPreference.weatherModelSet}")
    }

}