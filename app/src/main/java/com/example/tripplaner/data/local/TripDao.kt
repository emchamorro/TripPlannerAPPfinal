package com.example.tripplaner.data.local

import androidx.room.*
import com.example.tripplaner.data.model.Trip
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object para la entidad Trip
 */
@Dao
interface TripDao {
    
    @Query("SELECT * FROM trips ORDER BY createdAt DESC")
    suspend fun getAllTrips(): List<Trip>
    
    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTripsByUserId(userId: Long): Flow<List<Trip>>
    
    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getTripById(tripId: Long): Trip?
    
    @Query("SELECT * FROM trips WHERE userId = :userId AND id = :tripId")
    suspend fun getTripByUserIdAndId(userId: Long, tripId: Long): Trip?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip): Long
    
    @Update
    suspend fun updateTrip(trip: Trip)
    
    @Delete
    suspend fun deleteTrip(trip: Trip)
    
    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun deleteTripById(tripId: Long)
    
    @Query("DELETE FROM trips WHERE userId = :userId")
    suspend fun deleteAllTripsByUserId(userId: Long)
} 