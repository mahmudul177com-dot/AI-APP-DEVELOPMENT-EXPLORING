package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val OdommoLightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = SurfaceLight,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = PrimaryBlueDark,
    secondary = SecondaryTeal,
    onSecondary = SurfaceLight,
    secondaryContainer = SecondaryTealLight,
    onSecondaryContainer = TextPrimary,
    tertiary = AccentAmber,
    onTertiary = TextPrimary,
    tertiaryContainer = AccentAmberLight,
    onTertiaryContainer = TextPrimary,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

@Composable
fun OdommoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = OdommoLightColorScheme,
        typography = OdommoTypography,
        content = content
    )
}
