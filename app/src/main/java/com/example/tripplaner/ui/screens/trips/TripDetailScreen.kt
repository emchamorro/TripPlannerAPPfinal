package com.example.tripplaner.ui.screens.trips

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tripplaner.TripPlannerApplication
import com.example.tripplaner.ui.navigation.Screen
import com.example.tripplaner.ui.utils.DateUtils
import com.example.tripplaner.ui.viewmodel.TripViewModel
import com.example.tripplaner.ui.viewmodel.TripsState

/**
 * Pantalla que muestra los detalles de un viaje específico con temática de viajes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    navController: NavController,
    tripId: Long,
    viewModel: TripViewModel
) {
    val selectedTrip by viewModel.selectedTrip.collectAsState()
    val tripsState by viewModel.tripsState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    LaunchedEffect(tripId) {
        viewModel.getTripById(tripId)
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
                            "Detalles del Viaje",
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
                                selectedTrip?.let { trip ->
                                    navController.navigate("${Screen.AddEditTrip.route}/${trip.id}")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = { showDeleteDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                selectedTrip?.let { trip ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // Header con logo animado
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
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
                        
                        // Card principal del destino
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.95f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = trip.destination,
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1976D2)
                                        )
                                        if (trip.tripDate != null) {
                                            Text(
                                                text = DateUtils.formatDate(trip.tripDate),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color(0xFF666666)
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = "Destino",
                                        tint = Color(0xFF1976D2),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                
                                if (!trip.comment.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = trip.comment,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFF555555)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Información detallada del viaje
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.95f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Información del Viaje",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1976D2)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                DetailRowWithIcon(
                                    icon = Icons.Default.Info,
                                    label = "Duración",
                                    value = "${trip.duration} días",
                                    iconColor = Color(0xFF4CAF50)
                                )
                                
                                DetailRowWithIcon(
                                    icon = Icons.Default.Info,
                                    label = "Presupuesto",
                                    value = "$${String.format("%.2f", trip.budget)}",
                                    iconColor = Color(0xFF4CAF50)
                                )
                                
                                DetailRowWithIcon(
                                    icon = Icons.Default.Info,
                                    label = "Transporte",
                                    value = trip.transportMode,
                                    iconColor = Color(0xFF2196F3)
                                )
                                
                                DetailRowWithIcon(
                                    icon = Icons.Default.Home,
                                    label = "Alojamiento",
                                    value = trip.accommodationType,
                                    iconColor = Color(0xFFFF9800)
                                )
                                
                                if (trip.tripDate != null) {
                                    DetailRowWithIcon(
                                        icon = Icons.Default.Info,
                                        label = "Fecha del Viaje",
                                        value = DateUtils.formatDate(trip.tripDate),
                                        iconColor = Color(0xFFE91E63)
                                    )
                                }
                                
                                DetailRowWithIcon(
                                    icon = Icons.Default.Create,
                                    label = "Creado",
                                    value = DateUtils.formatDate(trip.createdAt),
                                    iconColor = Color(0xFF9C27B0)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Botones de acción
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    selectedTrip?.let { trip ->
                                        navController.navigate("${Screen.AddEditTrip.route}/${trip.id}")
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1976D2)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar")
                            }
                            
                            Button(
                                onClick = { showDeleteDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF44336)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Eliminar")
                            }
                        }
                    }
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.95f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xFF1976D2),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Cargando detalles...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF666666)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Diálogo de confirmación para eliminar
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar Viaje") },
                text = { Text("¿Estás seguro de que quieres eliminar este viaje? Esta acción no se puede deshacer.") },
                confirmButton = {
                    Button(
                        onClick = {
                            selectedTrip?.let { trip ->
                                viewModel.deleteTrip("", trip.id) // Aquí deberías pasar el token real
                                navController.popBackStack()
                            }
                            showDeleteDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF44336)
                        )
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

/**
 * Fila de detalle con ícono, etiqueta y valor
 */
@Composable
fun DetailRowWithIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    iconColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
        }
    }
}

 