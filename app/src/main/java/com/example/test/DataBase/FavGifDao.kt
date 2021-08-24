package com.example.test.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.test.model.Gif

@Dao
interface FavGifDao {
    @Insert
    fun insertFavGif(gif: Gif?)

    @Query("DELETE FROM gif WHERE idGif LIKE :idGif")
    fun removeFavGif(idGif: String?)

    @Query("SELECT * FROM gif")
    fun getAllFavGif(): MutableList<Gif>


}
