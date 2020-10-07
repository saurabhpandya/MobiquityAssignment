package com.mobiquityassignment.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobiquityassignment.data.map.WeatherNetworkDataProvider
import com.mobiquityassignment.data.map.WeatherRepository
import com.mobiquityassignment.ui.favorite.viewmodel.FavoriteViewModel
import com.mobiquityassignment.ui.map.viewmodel.MapViewModel

class ViewModelFactory
<T>(private val dataProvider: T, private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(
                application,
                WeatherRepository(dataProvider as WeatherNetworkDataProvider)
            ) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(
                application,
                WeatherRepository(dataProvider as WeatherNetworkDataProvider)
            ) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}