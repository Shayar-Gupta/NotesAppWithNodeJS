package com.example.notesappwithnodejs.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun SetUpNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HomeScreen) {

        composable<HomeScreen> {
            val viewModel: ViewModelHomeScreen = koinViewModel()
            val state by viewModel.getNotesState.collectAsState()
            HomeScreen(navController = navController, state = state, viewModel = viewModel)
        }

        composable<AddNotesScreen> { backStackEntry ->
            val viewModel: ViewModelAddNoteScreen = koinViewModel()
            val state by viewModel.notesState.collectAsState()

            // All note data arrives directly in the route — no second ViewModel needed
            val args = backStackEntry.toRoute<AddNotesScreen>()

            LaunchedEffect(args.noteId) {
                if (args.noteId != null) {
                    // Pre-fill the form using the data that came with the route
                    viewModel.onEvent(
                        EventAddNoteScreen.LoadNote(
                            id = args.noteId,
                            title = args.noteTitle ?: "",
                            description = args.noteDescription ?: "",
                            priority = args.notePriority ?: "Low"
                        )
                    )
                }
            }

            AddNotesScreen(
                navController = navController,
                state = state,
                event = viewModel::onEvent
            )
        }
    }
}