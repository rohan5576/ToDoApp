package com.example.todoapp.data

import com.example.data.local.dao.ToDoDao
import com.example.data.local.entity.ToDoEntity
import com.example.todoapp.data.repository.ToDoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


@ExperimentalCoroutinesApi
class ToDoRepositoryTest {

    private lateinit var todoDao: ToDoDao
    private lateinit var repository: ToDoRepository

    @Before
    fun setUp() {
        todoDao = mock(ToDoDao::class.java)
        repository = ToDoRepository(todoDao)
    }

    @Test
    fun `getAllTodos returns flow of todos from dao`() = runTest {
        val expectedTodos = listOf(ToDoEntity(1, "Sample Todo"))
        `when`(todoDao.getAllTodos()).thenReturn(flowOf(expectedTodos))

        val todosFlow = repository.getAllTodos()

        todosFlow.collect { todos ->
            assert(todos == expectedTodos)
        }
    }

    @Test
    fun `addTodo calls dao insert`() = runTest {
        val todo = ToDoEntity(1, "Sample Todo")

        repository.addTodo(todo)
        verify(todoDao).insert(todo)
    }
}
