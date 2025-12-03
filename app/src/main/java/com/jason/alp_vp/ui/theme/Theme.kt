package com.jason.alp_vp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NeonBlue,
    secondary = NeonGreen,
    tertiary = NeonBlue,
    background = DeepNavy,
    surface = DarkGray,
    onPrimary = White,
    onSecondary = DeepNavy,
    onTertiary = White,
    onBackground = White,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ALPVPTheme(
    darkTheme: Boolean = true, // Always use dark theme for cyberpunk aesthetic
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable dynamic color
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // Always use dark color scheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}