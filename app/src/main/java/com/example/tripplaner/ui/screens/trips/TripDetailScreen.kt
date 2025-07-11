package com.example.tripplaner.ui.screens.trips

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tripplaner.TripPlannerApplication
import com.example.tripplaner.ui.navigation.Screen
import com.example.tripplaner.ui.viewmodel.TripViewModel
import com.example.tripplaner.ui.viewmodel.TripsState

/**
 * Pantalla que muestra los detalles de un viaje específico
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    navController: NavController,
    tripId: Long,
    viewModel: TripViewModel
) {
    val selectedTrip by viewModel.selectedTrip.collectAsState()
    
    LaunchedEffect(tripId) {
        viewModel.getTripById(tripId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Viaje") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            selectedTrip?.let { trip ->
                                navController.navigate("${Screen.AddEditTrip.route}/${trip.id}")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            selectedTrip?.let { trip ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = trip.destination,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = trip.comment ?: "Sin descripción",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Información del Viaje",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            DetailRow("Destino", trip.destination)
                            DetailRow("Duración", "${trip.duration} días")
                            DetailRow("Presupuesto", "$${trip.budget}")
                            DetailRow("Transporte", trip.transportMode)
                            DetailRow("Alojamiento", trip.accommodationType)
                            
                            trip.comment?.let { comment ->
                                if (comment.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    DetailRow("Comentarios", comment)
                                }
                            }
                        }
                    }
                }
            } ?: run {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

/**
 * Fila de detalle con etiqueta y valor
 */
@Composable
fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 