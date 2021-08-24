package com.example.test.DataBase

import android.content.Context
import androidx.room.Room

object DataBase {
    lateinit var database: FavoriteDatabase

    fun getDatabase(context: Context): FavoriteDatabase? {
        if (!::database.isInitialized) {
            database = Room.databaseBuilder(
                context,
                FavoriteDatabase::class.java,
                "Favorite Database"
            ).fallbackToDestructiveMigration()
                .build()
        }
        return database
    }
}