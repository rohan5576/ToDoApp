package com.example.domain.usecases

import com.example.data.local.entity.ToDoEntity
import com.example.data.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetToDoUseCase @Inject constructor(
    private val todoRepository: ToDoRepository
) {
    fun execute(): Flow<List<ToDoEntity>> = todoRepository.getAllTodos()
}