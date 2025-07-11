package com.example.tripplaner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de datos para representar un usuario en la aplicación
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val email: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
) 