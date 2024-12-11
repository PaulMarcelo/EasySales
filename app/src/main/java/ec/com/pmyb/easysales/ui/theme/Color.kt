package ec.com.pmyb.easysales.ui.theme

import androidx.compose.ui.graphics.Color

//object AppColors {
//    val White = Color(0xFFFFFFFF)
//    val Gray = Color(0xFFE3E7E7)
//    val Gray1 = Color(0xFF3BB9F5)
//    val Black = Color(0xFF000000)
//    val BlueF = Color(0xFF35A9E0)
//    val CremaF = Color(0xFFECA97F)
//
//}

object AppColors {
    val BlueF = Color(0xFF4578C7)
//    val BlueF = Color(0xFF35A9E0)
    val CremaF = Color(0xFFECA97F)
    val CremaF_1 = Color(0xFFC08561)
    val Gray0 = Color(0xFF494747)
    val IndigoBlueLight = Color(0xFFAAB1E2)

    val White = Color(0xFFFCFDFD)
    val White01 = Color(0xFFC4C7C7)
    val Black = Color(0xFF020202)
    val Black01 = Color(0xFF1F1E1E)
    val Black02 = Color(0xFF2B2222)

    val PinkLight = Color(0xFFE7DFE1)
    val PinkLight2 = Color(0xFFACA6B1)
    val Red = Color(0xFFB13333)
    val Gray1 = Color(0xFFDFD7CB)
    val Gray2 = Color(0xFF504C4B)
    val Gray3 = Color(0xFF979592)
    val Gray4 = Color(0xFF504C4B)
    val Gray5 = Color(0xFF8F8F8F)



}

sealed class ThemeColors(
    val bacground: Color,
    val surafce: Color,
    val primary: Color,
    val text: Color
)  {
    object Night: ThemeColors(
        surafce = AppColors.BlueF,
        bacground = AppColors.Black01,
        primary = AppColors.BlueF,
        text = Color.White
    )
    object Day: ThemeColors(
        surafce = AppColors.IndigoBlueLight,
        bacground = AppColors.White01,
        primary = AppColors.IndigoBlueLight,
        text = Color.Black
    )
}