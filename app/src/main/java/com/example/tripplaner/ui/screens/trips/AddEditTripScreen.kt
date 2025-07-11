package com.example.tripplaner.ui.screens.trips

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tripplaner.TripPlannerApplication
import com.example.tripplaner.data.model.Trip
import com.example.tripplaner.ui.navigation.Screen
import com.example.tripplaner.ui.utils.DateUtils
import com.example.tripplaner.ui.viewmodel.AuthViewModel
import com.example.tripplaner.ui.viewmodel.TripViewModel
import com.example.tripplaner.ui.viewmodel.TripsState

/**
 * Pantalla para agregar o editar un viaje con temática de viajes
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
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    
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
            selectedDate = trip.tripDate
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
    
    // Gradiente de fondo temático de viajes
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1976D2), // Azul principal
            Color(0xFF42A5F5), // Azul claro
            Color(0xFF90CAF9)  // Azul muy claro
        )
    )
    
    // Animación del logo
    val infiniteTransition = rememberInfiniteTransition()
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            if (isEditing) "Editar Viaje" else "Nuevo Viaje",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
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
                                    tripDate = selectedDate,
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
                                contentDescription = "Guardar",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header con logo animado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .size(80.dp)
                            .scale(logoScale),
                        shape = RoundedCornerShape(40.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Logo de viaje",
                                tint = Color(0xFF1976D2),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
                
                // Formulario
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Información del Viaje",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1976D2)
                        )
                        
                        OutlinedTextField(
                            value = destination,
                            onValueChange = { destination = it },
                            label = { Text("Destino") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = Color(0xFF1976D2)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1976D2),
                                unfocusedBorderColor = Color(0xFFBDBDBD)
                            )
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
                                singleLine = true,
                                                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF4CAF50)
                                )
                            },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    unfocusedBorderColor = Color(0xFFBDBDBD)
                                )
                            )
                            
                            OutlinedTextField(
                                value = budget,
                                onValueChange = { budget = it },
                                label = { Text("Presupuesto") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF4CAF50)
                                )
                            },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    unfocusedBorderColor = Color(0xFFBDBDBD)
                                )
                            )
                        }
                        
                        OutlinedTextField(
                            value = transportMode,
                            onValueChange = { transportMode = it },
                            label = { Text("Medio de transporte") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF2196F3)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2196F3),
                                unfocusedBorderColor = Color(0xFFBDBDBD)
                            )
                        )
                        
                        OutlinedTextField(
                            value = accommodationType,
                            onValueChange = { accommodationType = it },
                            label = { Text("Tipo de alojamiento") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    tint = Color(0xFFFF9800)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFF9800),
                                unfocusedBorderColor = Color(0xFFBDBDBD)
                            )
                        )
                        
                        // Campo de fecha (simplificado)
                        Button(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE91E63)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedDate?.let { DateUtils.formatDate(it) } ?: "Seleccionar fecha del viaje"
                            )
                        }
                        
                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            label = { Text("Comentarios adicionales (opcional)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF9C27B0)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF9C27B0),
                                unfocusedBorderColor = Color(0xFFBDBDBD)
                            )
                        )
                        
                        if (tripsState is TripsState.Error) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF44336).copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFF44336),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = (tripsState as TripsState.Error).message,
                                        color = Color(0xFFF44336),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        
                        if (tripsState is TripsState.Loading) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xFF1976D2),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        // DatePicker simplificado
        if (showDatePicker) {
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                title = { Text("Seleccionar fecha") },
                text = { Text("Funcionalidad de fecha próximamente disponible") },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
} 