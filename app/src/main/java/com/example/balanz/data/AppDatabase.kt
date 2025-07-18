package com.example.balanz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.balanz.data.dao.TransactionDao
import com.example.balanz.data.entity.Transaction

@Database(entities = [Transaction::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "balanz_database"
                )
                    .fallbackToDestructiveMigration() // Allow destructive migrations for development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}