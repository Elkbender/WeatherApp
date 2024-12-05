package no.bitfield.weather.topbar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

enum class SearchWidgetState {
    OPENED, CLOSED
}

@HiltViewModel
class TopBarViewModel @Inject constructor() : ViewModel() {
    private val _searchWidgetState = MutableStateFlow(SearchWidgetState.CLOSED)
    val searchWidgetState: StateFlow<SearchWidgetState> = _searchWidgetState

    private val _searchTextState = MutableStateFlow("")
    val searchTextState: StateFlow<String> = _searchTextState

    fun toggleSearchWidget() {
        _searchWidgetState.value = when (_searchWidgetState.value) {
            SearchWidgetState.CLOSED -> SearchWidgetState.OPENED
            SearchWidgetState.OPENED -> SearchWidgetState.CLOSED
        }
    }

    fun updateSearchTextState(searchText: String) {
        _searchTextState.value = searchText
    }
}