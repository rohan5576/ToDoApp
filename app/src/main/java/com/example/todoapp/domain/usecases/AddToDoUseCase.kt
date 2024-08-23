package com.example.todoapp.domain.usecases

import com.example.todoapp.data.local.entity.ToDoEntity
import com.example.todoapp.data.repository.ToDoRepository
import javax.inject.Inject

class AddToDoUseCase @Inject constructor(
    private val todoRepository: ToDoRepository
) {
    suspend fun execute(todo: ToDoEntity) {
        todoRepository.addTodo(todo)
    }
}