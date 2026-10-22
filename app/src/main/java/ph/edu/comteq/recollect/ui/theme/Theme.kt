package ph.edu.comteq.recollect.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RecollectColors = lightColorScheme(
    primary = DeepTeal,
    onPrimary = Color.White,
    primaryContainer = TealTint,
    onPrimaryContainer = Ink,
    secondary = AccentOrange,
    onSecondary = Color(0xFF2B1200),
    tertiary = BrandTeal,
    onTertiary = Color(0xFF04292E),
    background = Paper,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = TealTint,
    onSurfaceVariant = Color(0xFF2E5258),
    surfaceContainerHighest = TealTint   // the default color of Card
)

@Composable
fun ReCollectTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RecollectColors,
        typography = Typography,
        content = content
    )
}

@Composable
fun RecollectTheme(content: @Composable () -> Unit) = ReCollectTheme(content = content)
