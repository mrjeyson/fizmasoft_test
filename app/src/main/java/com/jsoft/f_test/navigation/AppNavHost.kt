package com.jsoft.f_test.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jsoft.f_test.feature.face.ui.FaceScreen
import com.jsoft.f_test.feature.login.ui.LoginScreen
import com.jsoft.f_test.navigation.home.HomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.Login,
    ) {
        composable<AppDestinations.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestinations.Face) {
                        popUpTo(AppDestinations.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<AppDestinations.Face> {
            FaceScreen(
                onFaceDetected = {
                    navController.navigate(AppDestinations.Home) {
                        popUpTo(AppDestinations.Face) { inclusive = true }
                    }
                }
            )
        }

        composable<AppDestinations.Home> {
            HomeScreen()
        }
   dsakldsakl }
}















