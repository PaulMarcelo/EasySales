package ec.com.pmyb.easysales.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ec.com.pmyb.easysales.R
import ec.com.pmyb.easysales.data.local.datastore.DataStoreManager
import ec.com.pmyb.easysales.presentation.navigation.SalesScreens
import ec.com.pmyb.easysales.presentation.utils.UtilColorApp
import kotlinx.coroutines.delay


@Composable
fun SalesSplashScreen(navController: NavController) {
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(context)
    val userName = dataStoreManager.userSessionFlow.collectAsState(initial = null)
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val colorText = UtilColorApp.getBackgroundSplashScreen(isSystemInDarkTheme)
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (progress < 1f) {
            progress += 0.02f
            delay(100)
        }
    }

    LaunchedEffect(Unit) {
        delay(5000)
        if (userName.value?.user == null) {
            navController.navigate(SalesScreens.LoginScreen.name)
        } else {
//            navController.navigate(SalesScreens.MainScreen.name)
            navController.navigate(SalesScreens.SaleScreen.name)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorText)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fudisa))
            val logoAnimationState =
                animateLottieCompositionAsState(composition = composition)

            LottieAnimation(
                composition = composition,
                progress = { logoAnimationState.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp, start = 10.dp, end = 10.dp)
            )

        }


    }
}

