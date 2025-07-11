package com.example.tripplaner.data.model

/**
 * Respuesta genérica de la API
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

/**
 * Respuesta de autenticación
 */
data class AuthResponse(
    val token: String,
    val user: User
)

/**
 * Respuesta de login
 */
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * Respuesta de registro
 */
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
) 