package com.example.tripplaner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de datos para representar un viaje en la aplicación
 */
@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val destination: String,
    val duration: Int,
    val budget: Double,
    val transportMode: String,
    val accommodationType: String,
    val comment: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) 