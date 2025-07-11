package com.example.tripplaner.data.repository

import com.example.tripplaner.data.api.ApiService
import com.example.tripplaner.data.local.TripDao
import com.example.tripplaner.data.model.Trip
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para manejar las operaciones de viajes
 * Implementa el patrón Repository para separar la lógica de datos
 */
class TripRepository(
    private val apiService: ApiService,
    private val tripDao: TripDao
) {
    
    /**
     * Obtiene todos los viajes de un usuario desde la base de datos local
     */
    fun getTripsByUserId(userId: Long): Flow<List<Trip>> {
        return tripDao.getTripsByUserId(userId)
    }
    
    /**
     * Obtiene un viaje por ID desde la base de datos local
     */
    suspend fun getTripById(tripId: Long): Trip? {
        return tripDao.getTripById(tripId)
    }
    
    /**
     * Obtiene un viaje específico de un usuario desde la base de datos local
     */
    suspend fun getTripByUserIdAndId(userId: Long, tripId: Long): Trip? {
        return tripDao.getTripByUserIdAndId(userId, tripId)
    }
    
    /**
     * Obtiene todos los viajes de un usuario desde la base de datos local
     */
    suspend fun getTripsFromServer(token: String): Result<List<Trip>> {
        return try {
            // Simular obtención de datos locales
            val trips = tripDao.getAllTrips()
            Result.success(trips)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene un viaje específico desde la base de datos local
     */
    suspend fun getTripFromServer(token: String, tripId: Long): Result<Trip> {
        return try {
            val trip = tripDao.getTripById(tripId)
            if (trip != null) {
                Result.success(trip)
            } else {
                Result.failure(Exception("Viaje no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Crea un nuevo viaje en la base de datos local
     */
    suspend fun createTrip(token: String, trip: Trip): Result<Trip> {
        return try {
            val tripId = tripDao.insertTrip(trip)
            val createdTrip = tripDao.getTripById(tripId)
            if (createdTrip != null) {
                Result.success(createdTrip)
            } else {
                Result.failure(Exception("Error al crear viaje"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Actualiza un viaje existente en la base de datos local
     */
    suspend fun updateTrip(token: String, tripId: Long, trip: Trip): Result<Trip> {
        return try {
            val updatedTrip = trip.copy(id = tripId)
            tripDao.updateTrip(updatedTrip)
            val result = tripDao.getTripById(tripId)
            if (result != null) {
                Result.success(result)
            } else {
                Result.failure(Exception("Error al actualizar viaje"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Elimina un viaje de la base de datos local
     */
    suspend fun deleteTrip(token: String, tripId: Long): Result<Unit> {
        return try {
            val trip = tripDao.getTripById(tripId)
            if (trip != null) {
                tripDao.deleteTrip(trip)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Viaje no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Guarda un viaje en la base de datos local
     */
    suspend fun saveTripLocally(trip: Trip): Long {
        return tripDao.insertTrip(trip)
    }
    
    /**
     * Actualiza un viaje en la base de datos local
     */
    suspend fun updateTripLocally(trip: Trip) {
        tripDao.updateTrip(trip)
    }
    
    /**
     * Elimina un viaje de la base de datos local
     */
    suspend fun deleteTripLocally(trip: Trip) {
        tripDao.deleteTrip(trip)
    }
} 