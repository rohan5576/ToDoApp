package com.example.todoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.utils.Constants.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String
)