package com.example.todoapp.usecases

import com.example.todoapp.data.local.entity.ToDoEntity
import com.example.todoapp.data.repository.ToDoRepository
import com.example.todoapp.domain.usecases.GetToDoUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetToDoUseCaseTest {

    private lateinit var getToDoUseCase: GetToDoUseCase
    private val todoRepository: ToDoRepository = mock(ToDoRepository::class.java)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getToDoUseCase = GetToDoUseCase(todoRepository)
    }

    @Test
    fun `execute should return the list of todos from repository`() = runBlocking {

        val expectedTodos = listOf(
            ToDoEntity(id = 1, "Test Todo 1"),
            ToDoEntity(id = 2, "Test Todo 2")
        )

        `when`(todoRepository.getAllTodos()).thenReturn(flowOf(expectedTodos))

        val result = getToDoUseCase.execute().toList()
        assertEquals(listOf(expectedTodos), result)
    }
}

