package com.example.todoapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todoapp.R
import com.example.todoapp.presentation.viewmodel.SharedViewModel
import com.example.todoapp.utils.Constants.DELAY_TIME_OUT
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToDoScreen(
    viewModel: SharedViewModel = hiltViewModel(), navController: NavController
) {
    val todoText by viewModel.todoText.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var hasStartedAdding by remember { mutableStateOf(false) }

    // Handle error display
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(
                message = it
            )
            viewModel.clearError()
            navController.popBackStack()
        }
    }

    if (isLoading && hasStartedAdding) {
        LaunchedEffect(Unit) {
            delay(DELAY_TIME_OUT)
            navController.popBackStack()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.add_todo_title)) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button),
                        tint = MaterialTheme.colorScheme.onPrimary // Bright color for dark mode
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            TextField(
                value = todoText,
                onValueChange = { newText -> viewModel.updateTodoText(newText) },
                label = { Text(stringResource(id = R.string.enter_todo_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

            Button(
                onClick = {
                    hasStartedAdding = true
                    viewModel.addTodo(todoText)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(id = R.string.add_todo_button))
            }

            if (hasStartedAdding && error == null) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
