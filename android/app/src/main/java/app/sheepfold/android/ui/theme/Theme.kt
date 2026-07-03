package app.sheepfold.android.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SheepfoldColorScheme = lightColorScheme(
    primary = Color(0xFFBDD7CE),
    onPrimary = Color(0xFF14352C),
    primaryContainer = Color(0xFFBDD7CE),
    onPrimaryContainer = Color(0xFF14352C),
    background = Color(0xFFBDD7CE),
    onBackground = Color(0xFF17211D),
    surface = Color(0xFFF6FAF8),
    onSurface = Color(0xFF17211D)
)

@Composable
fun OvcharnyaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SheepfoldColorScheme,
        content = content
    )
}
