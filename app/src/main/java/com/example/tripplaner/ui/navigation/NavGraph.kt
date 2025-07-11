package com.example.tripplaner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tripplaner.TripPlannerApplication
import com.example.tripplaner.ui.screens.auth.LoginScreen
import com.example.tripplaner.ui.screens.auth.RegisterScreen
import com.example.tripplaner.ui.screens.trips.AddEditTripScreen
import com.example.tripplaner.ui.screens.trips.TripDetailScreen
import com.example.tripplaner.ui.screens.trips.TripsListScreen

/**
 * Grafo de navegación principal de la aplicación
 */
@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val appContainer = (context.applicationContext as TripPlannerApplication).appContainer
    
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Pantallas de autenticación
        composable(Screen.Login.route) {
            val authViewModel = remember { appContainer.createAuthViewModel() }
            LoginScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        
        composable(Screen.Register.route) {
            val authViewModel = remember { appContainer.createAuthViewModel() }
            RegisterScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        
        // Pantallas de viajes
        composable(Screen.TripsList.route) {
            val tripViewModel = remember { appContainer.createTripViewModel() }
            val authViewModel = remember { appContainer.createAuthViewModel() }
            TripsListScreen(
                navController = navController,
                tripViewModel = tripViewModel,
                authViewModel = authViewModel
            )
        }
        
        composable(
            route = Screen.TripDetail.route + "/{tripId}",
            arguments = listOf(
                navArgument("tripId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getLong("tripId") ?: 0L
            val tripViewModel = remember { appContainer.createTripViewModel() }
            TripDetailScreen(
                navController = navController,
                tripId = tripId,
                viewModel = tripViewModel
            )
        }
        
        composable(
            route = Screen.AddEditTrip.route + "/{tripId}",
            arguments = listOf(
                navArgument("tripId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getLong("tripId") ?: 0L
            val tripViewModel = remember { appContainer.createTripViewModel() }
            val authViewModel = remember { appContainer.createAuthViewModel() }
            AddEditTripScreen(
                navController = navController,
                tripId = tripId,
                tripViewModel = tripViewModel,
                authViewModel = authViewModel
            )
        }
        
        // Ruta para agregar nuevo viaje (sin tripId)
        composable(Screen.AddEditTrip.route) {
            val tripViewModel = remember { appContainer.createTripViewModel() }
            val authViewModel = remember { appContainer.createAuthViewModel() }
            AddEditTripScreen(
                navController = navController,
                tripId = 0L,
                tripViewModel = tripViewModel,
                authViewModel = authViewModel
            )
        }
    }
} 