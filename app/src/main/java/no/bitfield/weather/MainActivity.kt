package no.bitfield.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import no.bitfield.weather.splashscreen.SplashScreenViewModel
import no.bitfield.weather.ui.theme.WeatherAppTheme
import no.bitfield.weather.weather.ui.WeatherScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashScreen: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { splashScreen.show.value }
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                WeatherScreen()
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreen() {
    WeatherScreen()
}