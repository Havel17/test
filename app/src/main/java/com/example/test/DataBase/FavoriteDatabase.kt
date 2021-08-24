package com.example.test.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.test.model.Gif

@Database(version = 1,entities = arrayOf(Gif::class))
abstract class FavoriteDatabase :RoomDatabase(){
    abstract fun getGifDao(): FavGifDao

}