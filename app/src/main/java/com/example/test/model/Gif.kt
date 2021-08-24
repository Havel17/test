package com.example.test.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = arrayOf(Index(value = ["name"], unique = true)))
data class Gif(
    @ColumnInfo(name = "idGif") var idGif: String?,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "url") var url: String?
){
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}
