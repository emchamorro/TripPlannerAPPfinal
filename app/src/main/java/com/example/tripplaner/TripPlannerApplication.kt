package com.example.tripplaner

import android.app.Application
import com.example.tripplaner.di.AppContainer

/**
 * Clase de aplicación principal
 */
class TripPlannerApplication : Application() {
    
    // Contenedor de dependencias
    lateinit var appContainer: AppContainer
        private set
    
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
} 