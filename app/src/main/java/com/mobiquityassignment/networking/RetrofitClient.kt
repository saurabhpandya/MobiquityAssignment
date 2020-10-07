package com.mobiquityassignment.networking

import com.mobiquityassignment.data.map.WeatherServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://api.openweathermap.org/data/2.5/weather/"

    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val client =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    val WEATHER_SERVICE: WeatherServices = getRetrofit().create(WeatherServices::class.java)

}