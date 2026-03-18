package com.example.notesappwithnodejs.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notesappwithnodejs.R
import com.example.notesappwithnodejs.components.AppOutlinedTextField
import com.example.notesappwithnodejs.components.AppToolBar
import com.example.notesappwithnodejs.components.CustomFilterChip
import com.example.notesappwithnodejs.domain.models.Notes

@Composable
fun AddNotesScreen(
    navController: NavHostController,
    state: StateAddNoteScreen,
    event: (EventAddNoteScreen) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Flips to true the moment the button is tapped — before any coroutine runs.
    // This is what actually prevents the double-save: it is synchronous and immediate.
    var isSubmitted by remember { mutableStateOf(false) }

    // Navigate back after success (no snackbar delay — instant)
    LaunchedEffect(state.notesSaved) {
        if (state.notesSaved == "Notes Saved!" || state.notesSaved == "Note Updated!") {
            navController.navigate(HomeScreen) {
                popUpTo(HomeScreen) { inclusive = true }
            }
        }
    }

    // Show snackbar only on error
    LaunchedEffect(state.notesError) {
        if (state.notesError.isNotBlank()) {
            isSubmitted = false   // re-enable the button so user can retry
            snackbarHostState.showSnackbar("Error: ${state.notesError}")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column {
            AppToolBar(
                title = if (state.isEditMode) "Edit Note" else "Add Note",
                navigationIcon = Icons.AutoMirrored.Default.ArrowBack,
                onNavigationClick = {
                    navController.navigate(HomeScreen) {
                        popUpTo(HomeScreen) { inclusive = true }
                    }
                }
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Spacer(modifier = Modifier.height(20.dp))

                AppOutlinedTextField(
                    value = state.notesTitle,
                    onValueChange = { event(EventAddNoteScreen.NoteTitle(it)) },
                    maxLines = 2,
                    label = "Note's Title"
                )
                if (state.titleError.isNotBlank()) {
                    Text(
                        text = state.titleError,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                AppOutlinedTextField(
                    value = state.notesDescription,
                    onValueChange = { event(EventAddNoteScreen.NoteDescription(it)) },
                    maxLines = 10,
                    height = 300.dp,
                    label = "Note's Description"
                )

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomFilterChip(
                        label = "Low",
                        color = Color(0xFF43A047),
                        selected = state.notesPriority == "Low",
                        onClick = { event(EventAddNoteScreen.NotePriority("Low")) }
                    )
                    CustomFilterChip(
                        label = "Medium",
                        color = Color(0xFFFFA726),
                        selected = state.notesPriority == "Medium",
                        onClick = { event(EventAddNoteScreen.NotePriority("Medium")) }
                    )
                    CustomFilterChip(
                        label = "High",
                        color = Color(0xFFE53935),
                        selected = state.notesPriority == "High",
                        onClick = { event(EventAddNoteScreen.NotePriority("High")) }
                    )
                }
            }
        }

        // Spinner while saving
        if (state.savingNotes) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colorResource(id = R.color.medium1_blue)
            )
        }

        // Error snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // FAB — disabled the instant it is tapped, stays disabled until screen closes
        FloatingActionButton(
            onClick = {
                if (isSubmitted) return@FloatingActionButton  // synchronous guard, blocks immediately
                isSubmitted = true                            // disable right away

                val notes = Notes(
                    notesTitle = state.notesTitle,
                    notesPriority = state.notesPriority,
                    noteDescription = state.notesDescription
                )

                if (state.isEditMode && state.editingNoteId != null) {
                    event(EventAddNoteScreen.UpdateNote(state.editingNoteId, notes))
                } else {
                    event(EventAddNoteScreen.SaveNote(notes))
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp),
            // visually dims when disabled so user knows it was registered
            containerColor = if (isSubmitted)
                colorResource(id = R.color.medium1_blue).copy(alpha = 0.4f)
            else
                colorResource(id = R.color.medium1_blue)
        ) {
            Icon(
                Icons.Filled.Done,
                contentDescription = "Save",
                tint = colorResource(id = R.color.white)
            )
        }
    }
}