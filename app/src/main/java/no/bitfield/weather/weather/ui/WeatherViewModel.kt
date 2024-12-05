package no.bitfield.weather.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.bitfield.weather.LocationTracker
import no.bitfield.weather.weather.data.MetResponse
import no.bitfield.weather.weather.data.WeatherResponse
import no.bitfield.weather.weather.network.MetApi
import no.bitfield.weather.weather.network.MetService
import no.bitfield.weather.weather.network.WeatherApi
import javax.inject.Inject


sealed class WeatherState {
    data object Loading : WeatherState()
    data class LocationSuccess(val weatherData: MetResponse) : WeatherState()
    data class NameSuccess(val weatherData: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationTracker: LocationTracker
) : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    init { getWeatherData("Oslo") }

    fun getWeatherData(city: String) {
        viewModelScope.launch {
            try {
                val weatherResponse = WeatherApi.weatherService.getWeather(
                    apiKey = "c4c9834ef0a048e1a4f45144231605",
                    location = city
                )
                _weatherState.value = WeatherState.NameSuccess(weatherResponse)
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error("Failed to fetch weather data.")
            }
        }
    }

    fun getWeatherForLocation() {
        viewModelScope.launch {
            locationTracker.getLocation()?.let {
                val apiInterface = MetApi.getInstance().create(MetService::class.java)
                val metResponse = apiInterface.getWeather(lat = it.latitude, lon = it.longitude)
                _weatherState.value = WeatherState.LocationSuccess(metResponse)
            }
        }
    }
}