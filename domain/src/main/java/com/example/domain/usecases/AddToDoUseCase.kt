package com.example.domain.usecases

import com.example.data.local.entity.ToDoEntity
import com.example.data.repository.ToDoRepository
import javax.inject.Inject

class AddToDoUseCase @Inject constructor(
    private val todoRepository: ToDoRepository
) {
    suspend fun execute(todo: ToDoEntity) {
        todoRepository.addTodo(todo)
    }
}