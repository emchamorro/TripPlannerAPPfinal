package com.example.tripplaner.ui.screens.trips

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tripplaner.TripPlannerApplication
import com.example.tripplaner.data.model.Trip
import com.example.tripplaner.ui.navigation.Screen
import com.example.tripplaner.ui.viewmodel.AuthViewModel
import com.example.tripplaner.ui.viewmodel.TripViewModel
import com.example.tripplaner.ui.viewmodel.TripsState

/**
 * Pantalla que muestra la lista de viajes del usuario
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsListScreen(
    navController: NavController,
    tripViewModel: TripViewModel,
    authViewModel: AuthViewModel
) {
    val tripsState by tripViewModel.tripsState.collectAsState()
    val trips by tripViewModel.trips.collectAsState()
    
    LaunchedEffect(Unit) {
        // Aquí deberías obtener el token del usuario autenticado
        // Por ahora usamos un token dummy
        tripViewModel.loadTripsFromServer("dummy_token")
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Viajes") },
                actions = {
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditTrip.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar viaje"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (tripsState) {
                is TripsState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is TripsState.Success -> {
                    if (trips.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No tienes viajes aún",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Toca el botón + para crear tu primer viaje",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(trips) { trip ->
                                TripCard(
                                    trip = trip,
                                    onClick = {
                                        navController.navigate("${Screen.TripDetail.route}/${trip.id}")
                                    }
                                )
                            }
                        }
                    }
                }
                is TripsState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error al cargar viajes",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = (tripsState as TripsState.Error).message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                tripViewModel.resetTripsState()
                                tripViewModel.loadTripsFromServer("dummy_token")
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
                else -> {
                    // Estado idle, no hacer nada
                }
            }
        }
    }
}

/**
 * Tarjeta que muestra la información de un viaje
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripCard(
    trip: Trip,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = trip.destination,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = trip.comment ?: "Sin descripción",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Destino: ${trip.destination}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Duración: ${trip.duration} días",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
} 