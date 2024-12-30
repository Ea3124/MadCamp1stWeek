package com.example.madcamp1stweek

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val nickName: String,
    val id: String,
    val password: String
)