package com.example.tripplaner.ui.navigation

/**
 * Rutas de navegación de la aplicación
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object TripsList : Screen("trips_list")
    object TripDetail : Screen("trip_detail")
    object AddEditTrip : Screen("add_edit_trip")
} 