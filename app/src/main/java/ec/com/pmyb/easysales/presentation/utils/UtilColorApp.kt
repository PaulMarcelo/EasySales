package ec.com.pmyb.easysales.presentation.utils

import androidx.compose.ui.graphics.Color
import ec.com.pmyb.easysales.ui.theme.AppColors

object UtilColorApp {
    fun getBackgroundSplashScreen(
        darkTheme: Boolean,
    ): Color {
        return if (darkTheme) {
            AppColors.PinkLight2
        } else {
            AppColors.White
        }
    }

    fun getTextColor(
        darkTheme: Boolean,
    ): Color {
        return if (darkTheme) {
            AppColors.White
        } else {
            AppColors.Black
        }
    }

    fun getTextDateColor(
        darkTheme: Boolean,
    ): Color {
        return if (darkTheme) {
            AppColors.Gray1
        } else {
            AppColors.Gray2
        }
    }
    fun getSubTextColor(
        darkTheme: Boolean,
    ): Color {
        return if (darkTheme) {
            AppColors.Gray3
        } else {
            AppColors.Gray4
        }
    }

    fun getIconColor(
        darkTheme: Boolean,
    ): Color {
        return if (darkTheme) {
            AppColors.White
        } else {
            AppColors.Black
        }
    }

    fun getModeAppPrincipalColor(
        darkTheme: Boolean,
    ): Color {
        return if (darkTheme) {
            AppColors.BlueF
        } else {
            AppColors.IndigoBlueLight
        }
    }

    fun backgroundDialogColor(
        darkTheme: Boolean,
    ): Color {
        return if (darkTheme) {
            AppColors.Black01
        } else {
            AppColors.PinkLight
        }
    }

    fun backgroundSearchBarColor(
        darkTheme: Boolean,
    ): Color {
        return if (darkTheme) {
            AppColors.Black02
        } else {
            AppColors.PinkLight
        }
    }

    fun backgroundCardItem(
        darkTheme: Boolean,
    ): Color {
        return if (darkTheme) {
            AppColors.Gray0
        } else {
            AppColors.PinkLight2
        }
    }

    fun backgroundPrincipal(
        darkTheme: Boolean,
    ): Color {
        return if (darkTheme) {
            AppColors.Gray0
        } else {
            AppColors.White
        }
    }



}