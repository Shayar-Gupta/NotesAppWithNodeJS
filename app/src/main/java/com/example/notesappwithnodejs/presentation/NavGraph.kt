package com.example.notesappwithnodejs.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun SetUpNavHost() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = HomeScreen) {
        composable<HomeScreen>{
            val viewModel : ViewModelHomeScreen = koinViewModel()
            val state by viewModel.getNotesState.collectAsState()
            HomeScreen(navController = navController, state = state)
        }

        composable<AddNotesScreen>{
            val viewModel : ViewModelAddNoteScreen = koinViewModel() /* = viewModel<ViewModelAddNoteScreen>()*/
            val state by viewModel.notesState.collectAsState()
            AddNotesScreen(navController, state, viewModel::onEvent /*{viewModel.onEvent(it) }*/ )
        }
    }
}
