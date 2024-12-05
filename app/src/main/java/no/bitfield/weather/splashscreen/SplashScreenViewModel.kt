package no.bitfield.weather.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor() : ViewModel(){
    private val _showSplash = MutableStateFlow(true)
    val show = _showSplash.asStateFlow()

    init {
        viewModelScope.launch {
            delay(750)
            _showSplash.value = false
        }
    }
}