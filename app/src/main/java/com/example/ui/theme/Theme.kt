package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val RulesOfWealthDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Color(0xFF261900),
    primaryContainer = GoldContainer,
    onPrimaryContainer = OnGoldContainer,

    secondary = CyanSecondary,
    onSecondary = Color(0xFF001F2A),
    secondaryContainer = CyanContainer,
    onSecondaryContainer = CyanLight,

    tertiary = EmeraldTertiary,
    onTertiary = Color(0xFF002114),
    tertiaryContainer = Color(0xFF003824),
    onTertiaryContainer = EmeraldLight,

    background = DarkBackground,
    onBackground = TextPrimary,

    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,

    outline = BorderSubtle,
    outlineVariant = Color(0xFF1E293B),

    error = ErrorRed,
    onError = Color.White
)

private val RulesOfWealthLightColorScheme = lightColorScheme(
    primary = GoldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE082),
    onPrimaryContainer = Color(0xFF261900),

    secondary = CyanDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBEE9F7),
    onSecondaryContainer = Color(0xFF001F2A),

    tertiary = EmeraldDark,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFA7F3D0),
    onTertiaryContainer = Color(0xFF002114),

    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),

    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),

    outline = Color(0xFF475569),
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek dark mode for game immersion
    dynamicColor: Boolean = false, // Keep consistent branding colors
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) RulesOfWealthDarkColorScheme else RulesOfWealthLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
