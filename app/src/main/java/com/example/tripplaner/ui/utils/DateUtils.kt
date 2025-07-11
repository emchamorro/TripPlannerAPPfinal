package com.example.tripplaner.ui.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utilidades para el manejo de fechas en la aplicación
 */
object DateUtils {
    
    /**
     * Formatea una fecha en milisegundos a un string legible
     */
    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(date)
    }
} 