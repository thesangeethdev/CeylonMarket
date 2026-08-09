package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Surface,
    primaryContainer = TealPrimary.copy(alpha = 0.12f),
    onPrimaryContainer = TealDark,
    secondary = AmberSecondary,
    onSecondary = Surface,
    background = Background,
    surface = Surface,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Divider
)

@Composable
fun CeylonMarketTheme(content: @Composable () -> Unit){
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}