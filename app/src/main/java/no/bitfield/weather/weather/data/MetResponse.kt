package no.bitfield.weather.weather.data

data class MetResponse(
	val geometry: Geometry,
	val type: String,
	val properties: Properties
)

data class Geometry(
	val coordinates: List<Double>,
	val type: String
)

data class Properties(
	val timeseries: List<TimeseriesItem>,
	val meta: Meta
)

data class TimeseriesItem(
	val data: Data,
	val time: String
)

data class Data(
	val instant: Instant,
	val next_6_hours: Next6Hours,
	val next_12_hours: Next12Hours,
	val next_1_hours: Next1Hours
)

data class Instant(
	val details: Details
)

data class Next1Hours(
	val summary: Summary,
	val details: Details
)

data class Next6Hours(
	val summary: Summary,
	val details: Details
)

data class Next12Hours(
	val summary: Summary,
	val details: Details
)

data class Summary(
	val symbol_code: String
)

data class Details(
	val air_pressure_at_sea_level: Double,
	val air_temperature: Double,
	val cloud_area_fraction: Double,
	val relative_humidity: Double,
	val wind_from_direction: Double,
	val wind_speed: Double,
	val precipitation_amount: Double
)

data class Meta(
	val updated_at: String,
	val units: Units
)

data class Units(
	val air_temperature: String,
	val air_pressure_at_sea_level: String,
	val precipitation_amount: String,
	val wind_speed: String,
	val cloud_area_fraction: String,
	val relative_humidity: String,
	val wind_from_direction: String
)

