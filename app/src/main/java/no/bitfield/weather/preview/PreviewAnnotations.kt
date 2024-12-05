package no.bitfield.weather.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class ThemesPreview

@Preview(name = "small font", group = "font scales", fontScale = 0.5f)
@Preview(name = "large font", group = "font scales", fontScale = 1.5f)
annotation class FontScalePreview