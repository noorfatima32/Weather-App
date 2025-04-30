package com.example.weatherapp
import com.example.weatherapp.Main
import com.example.weatherapp.Weather


data class ForecastResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val main: Main,
    val weather: List<Weather>
)


