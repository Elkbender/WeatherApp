package no.bitfield.weather.weather.data

data class WeatherResponse(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val country: String,
    val region: String
)

data class Current(
    val temp_c: Double,
    val humidity: Int,
    val feelslike_c: Float,
    val cloud: String,
    val wind_kph: String,
    val condition: Condition,
)

data class Condition(
    val text: String,
    val icon: String
)