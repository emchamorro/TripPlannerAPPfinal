package com.example.tripplaner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplaner.data.model.Trip
import com.example.tripplaner.data.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar las operaciones de viajes
 * Implementa el patrón MVVM para separar la lógica de presentación
 */
class TripViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {
    
    private val _tripsState = MutableStateFlow<TripsState>(TripsState.Idle)
    val tripsState: StateFlow<TripsState> = _tripsState.asStateFlow()
    
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()
    
    private val _selectedTrip = MutableStateFlow<Trip?>(null)
    val selectedTrip: StateFlow<Trip?> = _selectedTrip.asStateFlow()
    
    /**
     * Carga los viajes de un usuario desde el servidor
     */
    fun loadTripsFromServer(token: String) {
        viewModelScope.launch {
            _tripsState.value = TripsState.Loading
            
            val result = tripRepository.getTripsFromServer(token)
            result.fold(
                onSuccess = { tripsList ->
                    _trips.value = tripsList
                    _tripsState.value = TripsState.Success
                },
                onFailure = { exception ->
                    _tripsState.value = TripsState.Error(exception.message ?: "Error al cargar viajes")
                }
            )
        }
    }
    
    /**
     * Observa los viajes de un usuario desde la base de datos local
     */
    fun observeTrips(userId: Long) {
        viewModelScope.launch {
            tripRepository.getTripsByUserId(userId).collect { tripsList ->
                _trips.value = tripsList
                _tripsState.value = TripsState.Success
            }
        }
    }
    
    /**
     * Crea un nuevo viaje
     */
    fun createTrip(token: String, trip: Trip) {
        viewModelScope.launch {
            _tripsState.value = TripsState.Loading
            
            val result = tripRepository.createTrip(token, trip)
            result.fold(
                onSuccess = { createdTrip ->
                    _tripsState.value = TripsState.Success
                },
                onFailure = { exception ->
                    _tripsState.value = TripsState.Error(exception.message ?: "Error al crear viaje")
                }
            )
        }
    }
    
    /**
     * Actualiza un viaje existente
     */
    fun updateTrip(token: String, tripId: Long, trip: Trip) {
        viewModelScope.launch {
            _tripsState.value = TripsState.Loading
            
            val result = tripRepository.updateTrip(token, tripId, trip)
            result.fold(
                onSuccess = { updatedTrip ->
                    _tripsState.value = TripsState.Success
                },
                onFailure = { exception ->
                    _tripsState.value = TripsState.Error(exception.message ?: "Error al actualizar viaje")
                }
            )
        }
    }
    
    /**
     * Elimina un viaje
     */
    fun deleteTrip(token: String, tripId: Long) {
        viewModelScope.launch {
            _tripsState.value = TripsState.Loading
            
            val result = tripRepository.deleteTrip(token, tripId)
            result.fold(
                onSuccess = {
                    _tripsState.value = TripsState.Success
                },
                onFailure = { exception ->
                    _tripsState.value = TripsState.Error(exception.message ?: "Error al eliminar viaje")
                }
            )
        }
    }
    
    /**
     * Selecciona un viaje para ver sus detalles
     */
    fun selectTrip(trip: Trip) {
        _selectedTrip.value = trip
    }
    
    /**
     * Limpia la selección de viaje
     */
    fun clearSelectedTrip() {
        _selectedTrip.value = null
    }
    
    /**
     * Obtiene un viaje por ID
     */
    fun getTripById(tripId: Long) {
        viewModelScope.launch {
            val trip = tripRepository.getTripById(tripId)
            _selectedTrip.value = trip
        }
    }
    
    /**
     * Resetea el estado de los viajes
     */
    fun resetTripsState() {
        _tripsState.value = TripsState.Idle
    }
}

/**
 * Estados posibles de los viajes
 */
sealed class TripsState {
    object Idle : TripsState()
    object Loading : TripsState()
    object Success : TripsState()
    data class Error(val message: String) : TripsState()
} 