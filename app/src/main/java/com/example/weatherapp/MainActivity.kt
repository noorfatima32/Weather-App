package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.locks.Condition
import java.util.Calendar
import androidx.core.content.ContextCompat



// fc6ce08d715036053011fb99c05ac87da
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        fetchWeatherData("Faisalabad")
        SearchCity()
    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response =
            retrofit.getWeatherData(cityName, "fc6ce08d715036053011fb99c05ac87d", "metric")
        response.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val seaLevel = responseBody.main.pressure
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min
                    Log.d("WeatherApp", "Weather condition received: $condition")

                    binding.temp.text = "$temperature°C"
                    binding.weather.text = condition
                    binding.maxTemp.text = "Max Temp: $maxTemp°C"
                    binding.minTemp.text = "Min Temp: $minTemp°C"
                    binding.humidity.text = "$humidity%"
                    binding.windspeed.text = "$windSpeed m/s"
                    binding.sunrise.text = "${time(sunRise)} "
                    binding.sunset.text = "${time((sunSet))}"
                    binding.sea.text = "$seaLevel hPa"
                    binding.condition.text = condition
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.Cityname.text = "$cityName"
                    //   Log.d("TAG", "onResponse: $temperature")
                    //val calendar = Calendar.getInstance()
                    //val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    //val isNight = hour < 6 || hour > 18 // Consider night between 6 PM - 6 AM
                    //val weatherCondition = response.body()?.weather?.get(0)?.description ?: "Clear Sky"

                    val currentTime = System.currentTimeMillis() / 1000  // Convert to seconds
                    val isNight = currentTime < sunRise || currentTime > sunSet
                    Log.d(
                        "WeatherApp",
                        "Sunrise: $sunRise, Sunset: $sunSet, Current Time: $currentTime, isNight: $isNight"
                    )

                    //changeImagesAccordingToWeatherCondition(condition)
                    Log.d(
                        "WeatherApp",
                        "Weather description from API: ${responseBody.weather.firstOrNull()?.description}"
                    )
                    changeImagesAccordingToWeatherCondition(condition, isNight)


                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }



    private fun changeImagesAccordingToWeatherCondition(conditions: String, isNight: Boolean) {
        Log.d("WeatherApp", "Updating UI - Condition: $conditions, isNight: $isNight")
        when (conditions.lowercase()) {
             "clear", "sunny", "clear sky" -> {
                Log.d("WeatherApp", "Applying Haze/Mist/Fog Background")
                runOnUiThread {
                    binding.root.setBackgroundResource(if (isNight) R.drawable.night else R.drawable.sunny)
                    binding.lottieAnimationView.setAnimation(if (isNight) R.raw.night else R.raw.sunny)
                }
                val textColor = if (isNight) R.color.light_gray else R.color.black
                binding.temp.setTextColor(ContextCompat.getColor(this, textColor))
                binding.Cityname.setTextColor(ContextCompat.getColor(this, textColor))
                binding.weather.setTextColor(ContextCompat.getColor(this, textColor))
                binding.timing.setTextColor(ContextCompat.getColor(this, textColor))
                binding.maxTemp.setTextColor(ContextCompat.getColor(this, textColor))
                binding.minTemp.setTextColor(ContextCompat.getColor(this, textColor))
                binding.date.setTextColor(ContextCompat.getColor(this, textColor))
                binding.day.setTextColor(ContextCompat.getColor(this, textColor))
                binding.humidity.setTextColor(ContextCompat.getColor(this, textColor))
                binding.humidity1.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sunrise.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sunrise1.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sunset.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sunset1.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sea.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sea1.setTextColor(ContextCompat.getColor(this, textColor))
                binding.windspeed.setTextColor(ContextCompat.getColor(this, textColor))
                binding.windspeed1.setTextColor(ContextCompat.getColor(this, textColor))
                binding.condition.setTextColor(ContextCompat.getColor(this, textColor))
                binding.condition1.setTextColor(ContextCompat.getColor(this, textColor))
            }

            "partly clouds", "clouds", "few clouds", "overcast", "foggy", "scattered clouds" -> {
                runOnUiThread {
                    binding.root.setBackgroundResource(R.drawable.cloudy)
                    binding.lottieAnimationView.setAnimation(if (isNight) R.raw.nightcloud else R.raw.cloudy)
                }
                val textColor = R.color.black
                binding.temp.setTextColor(ContextCompat.getColor(this, textColor))
                binding.Cityname.setTextColor(ContextCompat.getColor(this, textColor))
                binding.weather.setTextColor(ContextCompat.getColor(this, textColor))
                binding.timing.setTextColor(ContextCompat.getColor(this, textColor))
                binding.maxTemp.setTextColor(ContextCompat.getColor(this, textColor))
                binding.minTemp.setTextColor(ContextCompat.getColor(this, textColor))
                binding.date.setTextColor(ContextCompat.getColor(this, textColor))
                binding.day.setTextColor(ContextCompat.getColor(this, textColor))
                binding.humidity.setTextColor(ContextCompat.getColor(this, textColor))
                binding.humidity1.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sunrise.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sunrise1.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sunset.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sunset1.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sea.setTextColor(ContextCompat.getColor(this, textColor))
                binding.sea1.setTextColor(ContextCompat.getColor(this, textColor))
                binding.windspeed.setTextColor(ContextCompat.getColor(this, textColor))
                binding.windspeed1.setTextColor(ContextCompat.getColor(this, textColor))
                binding.condition.setTextColor(ContextCompat.getColor(this, textColor))
                binding.condition1.setTextColor(ContextCompat.getColor(this, textColor))
            }

            "light rain", "drizzle", "moderate rain", "showers", "heavy rain", "rain", "shower rain" -> {
                runOnUiThread {
                    binding.root.setBackgroundResource(R.drawable.rain)
                    binding.lottieAnimationView.setAnimation(R.raw.rain)
                }
            }

            "light snow", "moderate snow", "heavy snow", "blizzard" -> {
                runOnUiThread {
                    binding.root.setBackgroundResource(R.drawable.snow)
                    binding.lottieAnimationView.setAnimation(R.raw.snow)
                }
            }
            "haze", "mist", "smoke", "fog" -> {
                runOnUiThread {
                    binding.root.setBackgroundResource(R.drawable.haze)
                    binding.lottieAnimationView.setAnimation(R.raw.haze)
                    binding.lottieAnimationView.playAnimation()
                }
            }
        }
        binding.lottieAnimationView.cancelAnimation()
            binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
       val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun time(timestamp: Long): String{
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }

    fun dayName(timestamp: Long): String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}

