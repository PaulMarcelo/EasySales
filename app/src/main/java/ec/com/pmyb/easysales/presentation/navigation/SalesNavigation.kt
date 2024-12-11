package ec.com.pmyb.easysales.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ec.com.pmyb.easysales.presentation.screens.income.IncomeScreen
import ec.com.pmyb.easysales.presentation.screens.login.LoginScreen
import ec.com.pmyb.easysales.presentation.screens.main.MainScreen
import ec.com.pmyb.easysales.presentation.screens.sales.SaleScreen
import ec.com.pmyb.easysales.presentation.screens.splash.SalesSplashScreen
import ec.com.pmyb.easysales.presentation.screens.supplier.ListSupplierScreen
import ec.com.pmyb.easysales.presentation.screens.supplier.SupplierScreen

@Composable
fun SalesNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController,startDestination = SalesScreens.SplashScreen.name){
        composable(SalesScreens.SplashScreen.name){
            SalesSplashScreen(navController = navController)
        }
        composable(SalesScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(SalesScreens.MainScreen.name) {
            MainScreen(navController = navController)
        }
        composable(SalesScreens.IncomeScreen.name) {
            IncomeScreen(navController = navController)
//            IncomeScreen()
        }

        composable(SalesScreens.SupplierScreen.name) {
            SupplierScreen(navController = navController)
        }
        composable(SalesScreens.ListSupplierScreen.name) {
            ListSupplierScreen(navController = navController)
        }

        composable(SalesScreens.SaleScreen.name) {
            SaleScreen(navController = navController)
        }
    }
}