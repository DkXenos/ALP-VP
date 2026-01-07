package com.jason.alp_vp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ZZZ/HSR Futuristic Dark Color Scheme
private val FuturisticDarkColorScheme = darkColorScheme(
    primary = AccentCyan,
    onPrimary = BackgroundDark,
    primaryContainer = AccentCyanDim,
    onPrimaryContainer = TextPrimary,

    secondary = AccentPurple,
    onSecondary = TextPrimary,
    secondaryContainer = AccentPurpleDim,
    onSecondaryContainer = TextPrimary,

    tertiary = AccentPurpleBright,
    onTertiary = TextPrimary,

    background = BackgroundDark,
    onBackground = TextPrimary,

    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDarkElevated,
    onSurfaceVariant = TextSecondary,

    error = StatusError,
    onError = TextPrimary,

    outline = BorderGlow,
    outlineVariant = BorderPurpleGlow,

    scrim = Color.Black.copy(alpha = 0.6f)
)

// Light color scheme (minimal changes as app focuses on dark theme)
private val LightColorScheme = lightColorScheme(
    primary = AccentCyanBright,
    secondary = AccentPurple,
    tertiary = Pink40,
    background = Color(0xFFF5F7FA),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun ALPVPTheme(
    darkTheme: Boolean = true, // Default to dark theme for futuristic look
    // Dynamic color is disabled for consistent branded experience
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> FuturisticDarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = BackgroundDark.toArgb()
            @Suppress("DEPRECATION")
            window.navigationBarColor = BackgroundDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}