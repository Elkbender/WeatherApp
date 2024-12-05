package no.bitfield.weather.weather.network

import no.bitfield.weather.weather.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("current.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
    ): WeatherResponse
}