package com.example.tripplaner.di

import android.content.Context
import androidx.room.Room
import com.example.tripplaner.data.api.ApiService
import com.example.tripplaner.data.local.AppDatabase
import com.example.tripplaner.data.local.TripDao
import com.example.tripplaner.data.local.UserDao
import com.example.tripplaner.data.model.User
import com.example.tripplaner.data.repository.TripRepository
import com.example.tripplaner.data.repository.UserRepository
import com.example.tripplaner.ui.viewmodel.AuthViewModel
import com.example.tripplaner.ui.viewmodel.TripViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Contenedor de dependencias para la aplicación
 * Maneja la creación y configuración de todas las dependencias
 */
class AppContainer(private val context: Context) {
    
    // Base de datos
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "trip_planner_database"
        ).build().also { db ->
            // Crear usuario de prueba
            CoroutineScope(Dispatchers.IO).launch {
                val testUser = User(
                    username = "demo_user",
                    email = "demo@example.com",
                    password = "123456"
                )
                db.userDao().insertUser(testUser)
            }
        }
    }
    
    // DAOs
    val userDao: UserDao by lazy { database.userDao() }
    val tripDao: TripDao by lazy { database.tripDao() }
    
    // Cliente HTTP
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    // Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.tripplanner.com/") // Cambiar por tu URL real
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // API Service
    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
    
    // Repositorios
    val userRepository: UserRepository by lazy { UserRepository(apiService, userDao) }
    val tripRepository: TripRepository by lazy { TripRepository(apiService, tripDao) }
    
    // ViewModels
    fun createAuthViewModel(): AuthViewModel {
        return AuthViewModel(userRepository)
    }
    
    fun createTripViewModel(): TripViewModel {
        return TripViewModel(tripRepository)
    }
} 