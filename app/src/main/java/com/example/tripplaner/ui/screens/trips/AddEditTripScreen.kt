package com.example.tripplaner.ui.screens.trips

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tripplaner.TripPlannerApplication
import com.example.tripplaner.data.model.Trip
import com.example.tripplaner.ui.navigation.Screen
import com.example.tripplaner.ui.viewmodel.AuthViewModel
import com.example.tripplaner.ui.viewmodel.TripViewModel
import com.example.tripplaner.ui.viewmodel.TripsState

/**
 * Pantalla para agregar o editar un viaje
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTripScreen(
    navController: NavController,
    tripId: Long = 0L,
    tripViewModel: TripViewModel,
    authViewModel: AuthViewModel
) {
    var destination by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var transportMode by remember { mutableStateOf("") }
    var accommodationType by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    
    val tripsState by tripViewModel.tripsState.collectAsState()
    val selectedTrip by tripViewModel.selectedTrip.collectAsState()
    
    val isEditing = tripId > 0
    
    LaunchedEffect(tripId) {
        if (isEditing) {
            tripViewModel.getTripById(tripId)
        }
    }
    
    LaunchedEffect(selectedTrip) {
        selectedTrip?.let { trip ->
            destination = trip.destination
            duration = trip.duration.toString()
            budget = trip.budget.toString()
            transportMode = trip.transportMode
            accommodationType = trip.accommodationType
            comment = trip.comment ?: ""
        }
    }
    
    LaunchedEffect(tripsState) {
        when (tripsState) {
            is TripsState.Success -> {
                navController.popBackStack()
            }
            is TripsState.Error -> {
                // El error se mostrará en la UI
            }
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Viaje" else "Nuevo Viaje") },
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
                            val trip = Trip(
                                id = if (isEditing) tripId else 0,
                                destination = destination,
                                duration = duration.toIntOrNull() ?: 0,
                                budget = budget.toDoubleOrNull() ?: 0.0,
                                transportMode = transportMode,
                                accommodationType = accommodationType,
                                comment = comment.takeIf { it.isNotBlank() },
                                userId = 1, // TODO: Obtener del usuario autenticado
                                createdAt = System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )
                            
                            if (isEditing) {
                                tripViewModel.updateTrip("dummy_token", tripId, trip)
                            } else {
                                tripViewModel.createTrip("dummy_token", trip)
                            }
                        },
                        enabled = destination.isNotBlank() && 
                                 duration.isNotBlank() && tripsState !is TripsState.Loading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Guardar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duración (días)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = budget,
                    onValueChange = { budget = it },
                    label = { Text("Presupuesto") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
            }
            
            OutlinedTextField(
                value = transportMode,
                onValueChange = { transportMode = it },
                label = { Text("Medio de transporte") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = accommodationType,
                onValueChange = { accommodationType = it },
                label = { Text("Tipo de alojamiento") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Comentarios adicionales (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )
            
            if (tripsState is TripsState.Error) {
                Text(
                    text = (tripsState as TripsState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            if (tripsState is TripsState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
} 