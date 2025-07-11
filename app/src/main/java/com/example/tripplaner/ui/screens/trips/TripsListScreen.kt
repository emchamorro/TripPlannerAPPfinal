package com.example.tripplaner.ui.screens.trips

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tripplaner.R
import com.example.tripplaner.TripPlannerApplication
import com.example.tripplaner.data.model.Trip
import com.example.tripplaner.ui.navigation.Screen
import com.example.tripplaner.ui.viewmodel.AuthViewModel
import com.example.tripplaner.ui.viewmodel.TripViewModel
import com.example.tripplaner.ui.viewmodel.TripsState
import java.text.SimpleDateFormat
import java.util.*

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
                title = { 
                    Text(
                        "Mis Viajes",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
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
                },
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
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
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE3F2FD),
                            Color(0xFFBBDEFB),
                            Color(0xFF90CAF9)
                        )
                    )
                )
        ) {
            // Background airplane image
            Image(
                painter = painterResource(id = R.drawable.airplane_background),
                contentDescription = "Airplane background",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.1f)
            )
            
            when (tripsState) {
                is TripsState.Loading -> {
                    // Animated loading indicator
                    val infiniteTransition = rememberInfiniteTransition()
                    val alpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(60.dp)
                                .alpha(alpha),
                            color = Color(0xFF1976D2),
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cargando tus viajes...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF1976D2),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                is TripsState.Success -> {
                    if (trips.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Animated empty state
                            val infiniteTransition = rememberInfiniteTransition()
                            val scale by infiniteTransition.animateFloat(
                                initialValue = 0.8f,
                                targetValue = 1.2f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(2000),
                                    repeatMode = RepeatMode.Reverse
                                )
                            )
                            
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Empty trips",
                                modifier = Modifier
                                    .size(120.dp)
                                    .alpha(0.6f),
                                tint = Color(0xFF1976D2)
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "¡Aún no tienes viajes!",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF1976D2),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Toca el botón + para crear tu primera aventura",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF1976D2).copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                            .padding(paddingValues)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Error",
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Error al cargar viajes",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = (tripsState as TripsState.Error).message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = {
                                tripViewModel.resetTripsState()
                                tripViewModel.loadTripsFromServer("dummy_token")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Reintentar",
                                fontWeight = FontWeight.Bold
                            )
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = trip.destination,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1976D2)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = trip.destination,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1976D2).copy(alpha = 0.7f)
                    )
                }
                
                // Transport icon
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Transport type",
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF1976D2)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF1976D2).copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Duración: ${trip.duration} días",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF1976D2).copy(alpha = 0.6f)
                    )
                }
                
                Text(
                    text = "Presupuesto: $${trip.budget}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1976D2)
                )
            }
            
            if (!trip.comment.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = trip.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1976D2).copy(alpha = 0.8f),
                    maxLines = 2
                )
            }
        }
    }
} 