package com.mobiquityassignment.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class WeatherModels(val arylstWeatherModel: ArrayList<WeatherModel>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherModel(
    @JsonProperty("coord")
    val coord: Coord? = null,
    @JsonProperty("weather")
    val weather: ArrayList<Weather>? = null,
    @JsonProperty("base")
    val base: String? = null,
    @JsonProperty("main")
    val main: Main? = null,
    @JsonProperty("visibility")
    val visibility: Int? = null,
    @JsonProperty("wind")
    val wind: Wind? = null,
    @JsonProperty("clouds")
    val clouds: Clouds? = null,
    @JsonProperty("dt")
    val dt: Long? = null,
    @JsonProperty("sys")
    val sys: Sys? = null,
    @JsonProperty("timezone")
    val timezone: Long? = null,
    @JsonProperty("id")
    val id: Long? = null,
    @JsonProperty("name")
    val name: String? = null,
    @JsonProperty("cod")
    val cod: Int? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Coord(
    @JsonProperty("lon")
    val lon: Double? = null,
    @JsonProperty("lat")
    val lat: Double? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Weather(
    @JsonProperty("id")
    val id: Int? = null,
    @JsonProperty("main")
    val main: String? = null,
    @JsonProperty("description")
    val description: String? = null,
    @JsonProperty("icon")
    val icon: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Main(
    @JsonProperty("temp")
    val temp: Double? = null,
    @JsonProperty("feels_like")
    val feels_like: Double? = null,
    @JsonProperty("temp_min")
    val temp_min: Double? = null,
    @JsonProperty("tem_max")
    val tem_max: Double? = null,
    @JsonProperty("pressure")
    val pressure: Int? = null,
    @JsonProperty("humidity")
    val humidity: Int? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Wind(
    @JsonProperty("speed")
    val speed: Double? = null,
    @JsonProperty("deg")
    val deg: Int? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Clouds(
    @JsonProperty("all")
    val all: Int? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Sys(
    @JsonProperty("type")
    val type: Int? = null,
    @JsonProperty("id")
    val id: Int? = null,
    @JsonProperty("country")
    val country: String? = null,
    @JsonProperty("sunrise")
    val sunrise: Long? = null,
    @JsonProperty("sunset")
    val sunset: Long? = null
)

