package com.example.tripplaner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplaner.data.model.AuthResponse
import com.example.tripplaner.data.model.LoginRequest
import com.example.tripplaner.data.model.RegisterRequest
import com.example.tripplaner.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la autenticación de usuarios
 * Implementa el patrón MVVM para separar la lógica de presentación
 */
class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<AuthResponse?>(null)
    val currentUser: StateFlow<AuthResponse?> = _currentUser.asStateFlow()
    
    /**
     * Registra un nuevo usuario
     */
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val registerRequest = RegisterRequest(username, email, password)
            val result = userRepository.registerUser(registerRequest)
            
            result.fold(
                onSuccess = { authResponse ->
                    _currentUser.value = authResponse
                    _authState.value = AuthState.Success(authResponse)
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Error en el registro")
                }
            )
        }
    }
    
    /**
     * Autentica un usuario existente
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val loginRequest = LoginRequest(email, password)
            val result = userRepository.loginUser(loginRequest)
            
            result.fold(
                onSuccess = { authResponse ->
                    _currentUser.value = authResponse
                    _authState.value = AuthState.Success(authResponse)
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Error en el login")
                }
            )
        }
    }
    
    /**
     * Cierra la sesión del usuario actual
     */
    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }
    
    /**
     * Resetea el estado de autenticación
     */
    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}

/**
 * Estados posibles de la autenticación
 */
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val authResponse: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
} 