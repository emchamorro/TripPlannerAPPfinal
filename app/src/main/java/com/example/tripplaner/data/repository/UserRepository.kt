package com.example.tripplaner.data.repository

import com.example.tripplaner.data.api.ApiService
import com.example.tripplaner.data.local.UserDao
import com.example.tripplaner.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para manejar las operaciones de usuarios
 * Implementa el patrón Repository para separar la lógica de datos
 */
class UserRepository(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    
    /**
     * Obtiene todos los usuarios desde la base de datos local
     */
    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }
    
    /**
     * Obtiene un usuario por ID desde la base de datos local
     */
    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
    
    /**
     * Obtiene un usuario por email desde la base de datos local
     */
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
    
    /**
     * Registra un nuevo usuario en la base de datos local
     */
    suspend fun registerUser(registerRequest: RegisterRequest): Result<AuthResponse> {
        return try {
            // Verificar si el usuario ya existe
            val existingUser = userDao.getUserByEmail(registerRequest.email)
            if (existingUser != null) {
                return Result.failure(Exception("El usuario ya existe"))
            }
            
            // Crear nuevo usuario
            val newUser = User(
                username = registerRequest.username,
                email = registerRequest.email,
                password = registerRequest.password
            )
            
            val userId = userDao.insertUser(newUser)
            val createdUser = userDao.getUserById(userId)
            
            if (createdUser != null) {
                val authResponse = AuthResponse(
                    token = "local_token_$userId",
                    user = createdUser
                )
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Error al crear usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Autentica un usuario desde la base de datos local
     */
    suspend fun loginUser(loginRequest: LoginRequest): Result<AuthResponse> {
        return try {
            val user = userDao.getUserByEmail(loginRequest.email)
            
            if (user != null && user.password == loginRequest.password) {
                val authResponse = AuthResponse(
                    token = "local_token_${user.id}",
                    user = user
                )
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Email o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Guarda un usuario en la base de datos local
     */
    suspend fun saveUserLocally(user: User): Long {
        return userDao.insertUser(user)
    }
    
    /**
     * Actualiza un usuario en la base de datos local
     */
    suspend fun updateUserLocally(user: User) {
        userDao.updateUser(user)
    }
    
    /**
     * Elimina un usuario de la base de datos local
     */
    suspend fun deleteUserLocally(user: User) {
        userDao.deleteUser(user)
    }
} 