package com.example.tripplaner.data.api

import com.example.tripplaner.data.model.*
import retrofit2.http.*

/**
 * Interfaz que define todos los endpoints de la API REST
 */
interface ApiService {
    
    // Endpoints de autenticación
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<AuthResponse>
    
    // Endpoints de viajes
    @GET("trips")
    suspend fun getTrips(@Header("Authorization") token: String): ApiResponse<List<Trip>>
    
    @GET("trips/{id}")
    suspend fun getTrip(
        @Header("Authorization") token: String,
        @Path("id") tripId: Long
    ): ApiResponse<Trip>
    
    @POST("trips")
    suspend fun createTrip(
        @Header("Authorization") token: String,
        @Body trip: Trip
    ): ApiResponse<Trip>
    
    @PUT("trips/{id}")
    suspend fun updateTrip(
        @Header("Authorization") token: String,
        @Path("id") tripId: Long,
        @Body trip: Trip
    ): ApiResponse<Trip>
    
    @DELETE("trips/{id}")
    suspend fun deleteTrip(
        @Header("Authorization") token: String,
        @Path("id") tripId: Long
    ): ApiResponse<Unit>
} 