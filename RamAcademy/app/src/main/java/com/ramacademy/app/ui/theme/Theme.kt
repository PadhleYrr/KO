package com.ramacademy.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── GKK-APP Theme Colors ───────────────────────────────────────────────────
val Navy        = Color(0xFF1A237E)   // --navy
val NavyDark    = Color(0xFF0D1557)
val NavyLight   = Color(0xFF283593)
val Saff        = Color(0xFFFF6B00)   // --saff (saffron/orange accent)
val SaffLight   = Color(0xFFFF8C3A)
val Gold        = Color(0xFFF9A825)   // --gold
val GoldLight   = Color(0xFFFFD54F)

val BgLight     = Color(0xFFF4F6FB)   // --bg
val CardLight   = Color(0xFFFFFFFF)   // --card
val BorderLight = Color(0xFFE2E8F0)   // --border
val TextPrimary = Color(0xFF1E293B)   // --text
val TextMuted   = Color(0xFF64748B)   // --muted

val Success     = Color(0xFF15803D)   // --success
val Danger      = Color(0xFFDC2626)   // --danger
val Warn        = Color(0xFFD97706)   // --warn

// Dark theme
val BgDark      = Color(0xFF0F172A)
val CardDark    = Color(0xFF1E293B)
val BorderDark  = Color(0xFF334155)

// ─── Light Color Scheme ──────────────────────────────────────────────────────
private val LightColors = lightColorScheme(
    primary          = Navy,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFEEF0FF),
    onPrimaryContainer = Navy,
    secondary        = Saff,
    onSecondary      = Color.White,
    secondaryContainer = Color(0xFFFFF3E0),
    onSecondaryContainer = Color(0xFFE65100),
    tertiary         = Gold,
    onTertiary       = Color.Black,
    background       = BgLight,
    onBackground     = TextPrimary,
    surface          = CardLight,
    onSurface        = TextPrimary,
    surfaceVariant   = Color(0xFFF8F9FF),
    onSurfaceVariant = TextMuted,
    outline          = BorderLight,
    error            = Danger,
    onError          = Color.White,
)

// ─── Dark Color Scheme ───────────────────────────────────────────────────────
private val DarkColors = darkColorScheme(
    primary          = Color(0xFF7986CB),
    onPrimary        = Color.White,
    primaryContainer = NavyDark,
    onPrimaryContainer = Color(0xFFBBC5FF),
    secondary        = SaffLight,
    onSecondary      = Color.Black,
    background       = BgDark,
    onBackground     = Color(0xFFE2E8F0),
    surface          = CardDark,
    onSurface        = Color(0xFFE2E8F0),
    outline          = BorderDark,
    error            = Color(0xFFFCA5A5),
)

@Composable
fun RamAcademyTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = RamTypography,
        content     = content
    )
}
