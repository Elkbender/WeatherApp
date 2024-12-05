package no.bitfield.weather.weather.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import no.bitfield.weather.R
import no.bitfield.weather.topbar.TopBar
import no.bitfield.weather.topbar.TopBarViewModel
import no.bitfield.weather.weather.data.Current
import no.bitfield.weather.weather.data.MetResponse
import no.bitfield.weather.weather.data.WeatherResponse

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    topBarViewModel: TopBarViewModel = hiltViewModel(),
) {
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val searchWidgetState by topBarViewModel.searchWidgetState.collectAsState()
    val searchTextState by topBarViewModel.searchTextState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                searchWidgetState = searchWidgetState,
                searchText = searchTextState,
                onTextChanged = { topBarViewModel.updateSearchTextState(it) },
                onCloseClicked = {
                    topBarViewModel.toggleSearchWidget()
                    topBarViewModel.updateSearchTextState("")
                },
                onSearchClicked = {
                    weatherViewModel.getWeatherData(city = it)
                    topBarViewModel.toggleSearchWidget()
                    topBarViewModel.updateSearchTextState("")
                },
                onSearchTriggered = { topBarViewModel.toggleSearchWidget() },
                onLocationClicked = { weatherViewModel.getWeatherForLocation() }
            )
        }
    ) { innerPadding ->
        WeatherInfo(weatherState = weatherState, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
private fun WeatherInfo(weatherState: WeatherState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (weatherState) {
            is WeatherState.LocationSuccess -> MetData(weatherData = weatherState.weatherData)
            is WeatherState.NameSuccess -> WeatherData(weatherData = weatherState.weatherData)
            is WeatherState.Loading -> CircularProgressIndicator()
            is WeatherState.Error -> Text(
                text = weatherState.message,
                fontSize = 18.sp,
                color = Color.Red
            )
        }
    }
}

@Composable
private fun MetData(weatherData: MetResponse) {
    with(weatherData) {
        val context = LocalContext.current
        val name = properties.timeseries[0].data.next_1_hours.summary.symbol_code
        val drawableId = remember(name) {
            context.resources.getIdentifier(name, "drawable", context.packageName)
        }

        Image(
            painterResource(id = drawableId),
            contentDescription = "Weather icon",
            modifier = Modifier
                .padding(20.dp)
                .size(128.dp)
        )

        Text(
            text = "lat: ${geometry.coordinates[1]}",
            fontSize = 45.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 55.sp,
        )
        Text(
            text = "long: ${geometry.coordinates[0]}",
            fontSize = 45.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 55.sp,
        )
        Text(
            text = "${properties.timeseries[0].data.instant.details.air_temperature}°C",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
        )
    }
}

@Composable
private fun WeatherData(weatherData: WeatherResponse) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandedState) 180f else 0f)

    with(weatherData) {
        Image(
            painter = rememberAsyncImagePainter("https:${current.condition.icon}"),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )

        Text(
            text = "${location.name}, ${location.country}",
            fontSize = 45.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 55.sp,
        )
        Text(
            text = "${current.temp_c}°C",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
        )
        Text(
            text = current.condition.text,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandedState = !expandedState }
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (expandedState) "Fewer details" else "More details")
            Icon(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .alpha(0.2f)
                    .rotate(rotationState),
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Drop-Down Arrow"
            )
        }

        AnimatedVisibility(
            visible = expandedState,
            modifier = Modifier.padding(top = 10.dp),
            enter = expandVertically(),
            exit = shrinkVertically()

        ) {
            ExtraWeatherDetails(weatherData = current)
        }

    }
}

@Composable
private fun ExtraWeatherDetails(weatherData: Current) {
    val itemsList = listOf(
        "Feels like: ${weatherData.feelslike_c}°C" to R.drawable.ic_feels_like_temp,
        "Humidity: ${weatherData.humidity}%" to R.drawable.ic_humidity,
        "Cloud cover: ${weatherData.cloud}%" to R.drawable.ic_cloud_cover,
        "Wind: ${weatherData.wind_kph} km/h" to R.drawable.ic_wind_speed
    )
    val columns = 2
    val rows = itemsList.chunked(columns)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        for (rowItems in rows) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                for (item in rowItems) {
                    WeatherDetailCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun WeatherDetailCard(item: Pair<String, Int>) {
    Card(
        modifier = Modifier
            .height(60.dp)
            .width(200.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = item.second),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(text = item.first)
        }
    }
}