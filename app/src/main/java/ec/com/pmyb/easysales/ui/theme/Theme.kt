package ec.com.pmyb.easysales.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
//    surface = AppColors.BlueF,
//    primary = AppColors.White,
//    onSurface = AppColors.White,

    primary = ThemeColors.Night.primary,
    onPrimary = ThemeColors.Night.text,
    surface = ThemeColors.Night.surafce,
    background = ThemeColors.Night.bacground

)

private val LightColorScheme = lightColorScheme(
//    surface = AppColors.IndigoBlueLight,
//    primary = AppColors.Black,
//    onSurface = AppColors.Black,
    primary = ThemeColors.Day.primary,
    onPrimary = ThemeColors.Day.text,
    surface = ThemeColors.Day.surafce,
    background = ThemeColors.Day.bacground

)

@Composable
fun EasySalesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}