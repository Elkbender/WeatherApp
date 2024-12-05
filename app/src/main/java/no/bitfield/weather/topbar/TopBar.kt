package no.bitfield.weather.topbar

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import no.bitfield.weather.preview.FontScalePreview
import no.bitfield.weather.preview.ThemesPreview

@OptIn(ExperimentalPermissionsApi::class)
@ThemesPreview
@FontScalePreview
@Composable
fun TopBar(
    searchWidgetState: SearchWidgetState = SearchWidgetState.CLOSED,
    searchText: String = "",
    onTextChanged: (String) -> Unit = {},
    onCloseClicked: () -> Unit = {},
    onSearchClicked: (String) -> Unit = {},
    onSearchTriggered: () -> Unit = {},
    onLocationClicked: () -> Unit = {},
) {
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> DefaultAppBar(
            onSearchClicked = onSearchTriggered,
            onLocationClicked = {
                if(!locationPermissions.allPermissionsGranted) {
                    locationPermissions.launchMultiplePermissionRequest()
                }
                onLocationClicked()
            }
        )

        SearchWidgetState.OPENED -> SearchAppBar(
            text = searchText,
            onTextChanged = onTextChanged,
            onCloseClicked = onCloseClicked,
            onSearchClicked = onSearchClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DefaultAppBar(
    onSearchClicked: () -> Unit,
    onLocationClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Weather") },
        actions = {
            IconButton(
                onClick = onSearchClicked
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon"
                )
            }
            IconButton(
                onClick = onLocationClicked
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location icon"
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchAppBar(
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TopAppBarDefaults.windowInsets.asPaddingValues()),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(TopAppBarDefaults.TopAppBarExpandedHeight)
                .background(Color.Transparent),
            value = text,
            onValueChange = { onTextChanged(it) },
            placeholder = { Text(text = "Search") },
            textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = { onSearchClicked(text) }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) onTextChanged("")
                        else onCloseClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon",
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions { onSearchClicked(text) },
        )
    }
}

@ThemesPreview
@FontScalePreview
@Composable
private fun TopBarPreview() {
    TopBar(searchWidgetState = SearchWidgetState.OPENED)
}