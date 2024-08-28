package com.example.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.core.ui.theme.Constants.MAIN_SCREEN
import com.example.core.ui.theme.Constants.TODO_ITEM_SCREEN
import com.example.presentation.screens.AddToDoScreen
import com.example.presentation.screens.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = MAIN_SCREEN) {
        composable(MAIN_SCREEN) { MainScreen(navController = navController) }
        composable(TODO_ITEM_SCREEN) { AddToDoScreen(navController = navController) }
    }
}