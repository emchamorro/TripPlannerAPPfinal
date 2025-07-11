package com.example.tripplaner.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.tripplaner.data.model.Trip
import com.example.tripplaner.data.model.User

/**
 * Base de datos principal de la aplicación usando Room
 */
@Database(
    entities = [User::class, Trip::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trip_planner_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 