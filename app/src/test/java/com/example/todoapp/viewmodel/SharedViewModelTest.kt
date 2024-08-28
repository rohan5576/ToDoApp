package com.example.todoapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todoapp.R
import com.example.data.local.entity.ToDoEntity
import com.example.domain.usecases.AddToDoUseCase
import com.example.domain.usecases.GetToDoUseCase
import com.example.presentation.viewmodel.SharedViewModel
import com.example.todoapp.utils.Constants.ERROR_TEXT
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


@ExperimentalCoroutinesApi
class SharedViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SharedViewModel
    private val getTodosUseCase = mock(GetToDoUseCase::class.java)
    private val addTodoUseCase = mock(AddToDoUseCase::class.java)
    private val resourceProvider = mock(ResourceProvider::class.java)
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Set the main dispatcher to a test dispatcher
        kotlinx.coroutines.Dispatchers.setMain(dispatcher)
        `when`(getTodosUseCase.execute()).thenReturn(flowOf(emptyList()))
        `when`(resourceProvider.getString(R.string.failed_to_add_todo)).thenReturn("Failed to add ToDo")

        viewModel = SharedViewModel(getTodosUseCase, addTodoUseCase, resourceProvider)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    @Test
    fun `updateQuery should update searchQuery state`() {
        viewModel.updateQuery("test query")
        assertEquals("test query", viewModel.searchQuery.value)
    }

    @Test
    fun `updateTodoText should update todoText state`() {
        viewModel.updateTodoText("test todo")
        assertEquals("test todo", viewModel.todoText.value)
    }

    @Test
    fun `addTodo should set error state when ERROR_TEXT is passed`() = runTest {
        viewModel.addTodo(ERROR_TEXT)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals("Failed to add ToDo", viewModel.error.value)
        assertTrue(viewModel.isLoading.value.not())
    }

    @Test
    fun `addTodo should add a new ToDo when valid text is passed`() = runTest {
        viewModel.addTodo("New ToDo")
        dispatcher.scheduler.advanceUntilIdle()

        verify(addTodoUseCase).execute(ToDoEntity(text = "New ToDo"))
        assertNull(viewModel.error.value)
    }

    @Test
    fun `list based on search query`() = runTest {
        `when`(getTodosUseCase.execute()).thenReturn(
            flowOf(
                listOf(
                    ToDoEntity(id = 1, text = "Task 1"),
                    ToDoEntity(id = 2, text = "Task 2"),
                    ToDoEntity(id = 3, text = "Another Task")
                )
            )
        )

        viewModel = SharedViewModel(getTodosUseCase, addTodoUseCase, resourceProvider)

        viewModel.updateQuery("Task")
        advanceUntilIdle()
        assertEquals(0, viewModel.filteredTodos.value.size)
    }

    @Test
    fun `clearError should clear the error state`() = runTest {
        viewModel.addTodo(ERROR_TEXT)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals("Failed to add ToDo", viewModel.error.value)

        viewModel.clearError()
        assertNull(viewModel.error.value)
    }
}
