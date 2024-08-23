package com.example.todoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.data.local.dao.ToDoDao
import com.example.todoapp.data.local.entity.ToDoEntity

@Database(entities = [ToDoEntity::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoDao
}