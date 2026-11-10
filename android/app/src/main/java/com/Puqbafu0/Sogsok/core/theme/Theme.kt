package com.Puqbafu0.Sogsok.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TrailColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = OnGold,
    primaryContainer = SurfaceWarm,
    onPrimaryContainer = GoldLight,
    secondary = GoldLight,
    onSecondary = OnGold,
    secondaryContainer = Surface2,
    onSecondaryContainer = GoldLight,
    tertiary = Jade,
    onTertiary = OnGold,
    tertiaryContainer = SurfaceWarm,
    onTertiaryContainer = TextPrimary,
    background = BgBase,
    onBackground = TextPrimary,
    surface = Surface1,
    onSurface = TextPrimary,
    surfaceVariant = Surface2,
    onSurfaceVariant = TextSecondary,
    outline = GoldRim,
    outlineVariant = TextMuted,
    error = Ember,
    onError = OnGold
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TrailColorScheme,
        typography = AppTypography,
        content = content
    )
}
