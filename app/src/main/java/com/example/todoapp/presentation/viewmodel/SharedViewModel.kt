package com.example.todoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.R
import com.example.todoapp.data.local.entity.ToDoEntity
import com.example.todoapp.di.ResourceProvider
import com.example.todoapp.domain.usecases.AddToDoUseCase
import com.example.todoapp.domain.usecases.GetToDoUseCase
import com.example.todoapp.utils.Constants.ERROR_TEXT
import com.example.todoapp.utils.Constants.TIME_OUT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getTodosUseCase: GetToDoUseCase,
    private val addTodoUseCase: AddToDoUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _todos = getTodosUseCase.execute()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIME_OUT), emptyList())

    val todos: StateFlow<List<ToDoEntity>> = _todos

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _todoText = MutableStateFlow("")
    val todoText: StateFlow<String> = _todoText.asStateFlow()

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateTodoText(text: String) {
        _todoText.value = text
    }

    val filteredTodos: StateFlow<List<ToDoEntity>> = combine(
        todos, searchQuery
    ) { todos, query ->
        todos.filter { it.text.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIME_OUT), emptyList())

    fun addTodo(text: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Simulate an error if the text is "Error"
                if (text.equals(ERROR_TEXT, ignoreCase = true)){
                    _error.emit(resourceProvider.getString(R.string.failed_to_add_todo))
                    _isLoading.value = false
                    throw Exception()
                }
                addTodoUseCase.execute(ToDoEntity(text = text))
            } catch (e: Exception) {
                _error.emit(resourceProvider.getString(R.string.failed_to_add_todo))
                _isLoading.value = false
            }
        }
    }

        fun clearError() {
        _error.value = null
    }
}
