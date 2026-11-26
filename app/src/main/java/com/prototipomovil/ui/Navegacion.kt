package com.prototipomovil.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prototipomovil.data.AppScreens

@Composable
fun Navegacion(
    sharedViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.PrimeraPantalla.route
    ) {
        composable(AppScreens.PrimeraPantalla.route) {
            PrimeraPantalla(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        composable(AppScreens.RegistroPantalla.route) {
            RegistroPantalla(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        composable(AppScreens.SegundaPantalla.route) {
            SegundaPantalla(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        composable(AppScreens.TerceraPantalla.route) {
            TerceraPantalla(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        composable(AppScreens.ListaTareasPantalla.route) {
            ListaTareasPantalla(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        composable(AppScreens.BuscarPantalla.route) {
            BuscarPantalla(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        composable(AppScreens.AlertasPantalla.route) {
            AlertasPantalla(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        composable(AppScreens.PerfilPantalla.route) {
            PerfilPantalla(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        composable(
            route = AppScreens.SeccionTareas.route,
            arguments = listOf(navArgument("categoria") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoria = backStackEntry.arguments?.getString("categoria") ?: "todas"
            SeccionTareasPantalla(
                categoria = categoria,
                navController = navController,
                viewModel = sharedViewModel
            )
        }
    }
}