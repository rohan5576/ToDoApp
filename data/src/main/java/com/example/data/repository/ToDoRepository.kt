package com.example.data.repository

import com.example.data.local.dao.ToDoDao
import com.example.data.local.entity.ToDoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToDoRepository @Inject constructor(
    private val todoDao: ToDoDao
) {
    fun getAllTodos(): Flow<List<ToDoEntity>> = todoDao.getAllTodos()

    suspend fun addTodo(todo: ToDoEntity) = todoDao.insert(todo)
}