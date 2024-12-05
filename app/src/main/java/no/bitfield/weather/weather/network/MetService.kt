package no.bitfield.weather.weather.network

import no.bitfield.weather.weather.data.MetResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MetService {
    @GET("weatherapi/locationforecast/2.0/compact")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): MetResponse
}