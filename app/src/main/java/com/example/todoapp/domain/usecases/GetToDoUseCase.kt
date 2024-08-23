package com.example.todoapp.domain.usecases

import com.example.todoapp.data.local.entity.ToDoEntity
import com.example.todoapp.data.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetToDoUseCase @Inject constructor(
    private val todoRepository: ToDoRepository
) {
    fun execute(): Flow<List<ToDoEntity>> = todoRepository.getAllTodos()
}