package com.example.notesappwithnodejs.presentation

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
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
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            AppToolBar(
                title = "Add Notes",
                navigationIcon = Icons.AutoMirrored.Default.ArrowBack,
                onNavigationClick = {
                    navController.navigate(HomeScreen) {
                        popUpTo(HomeScreen) { inclusive = true } //removes circular navigation
                    }
                }
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Spacer(modifier = Modifier.height(20.dp))
                AppOutlinedTextField(
                    value = state.notesTitle,
                    onValueChange = { title ->
                        event(EventAddNoteScreen.NoteTitle(title))
                    },
                    maxLines = 2,
                    label = "Note's Title"
                )

                Spacer(modifier = Modifier.height(30.dp))
                AppOutlinedTextField(
                    value = state.notesDescription,
                    onValueChange = { desc ->
                        event(EventAddNoteScreen.NoteDescription(desc))
                    },
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
                        color = Color.Green,
                        selected = state.notesPriority == "Low",
                        onClick = {
                            event(EventAddNoteScreen.NotePriority("Low"))
                        }
                    )

                    CustomFilterChip(
                        label = "Medium",
                        color = Color.Green,
                        selected = state.notesPriority == "Medium",
                        onClick = {
                            event(EventAddNoteScreen.NotePriority("Medium"))
                        }
                    )

                    CustomFilterChip(
                        label = "High",
                        color = Color.Green,
                        selected = state.notesPriority == "High",
                        onClick = {
                            event(EventAddNoteScreen.NotePriority("High"))
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                val notes = Notes(
                    notesTitle = state.notesTitle,
                    notesPriority = state.notesPriority,
                    noteDescription = state.notesDescription
                )

                event(EventAddNoteScreen.SaveNote(notes))

                when{
                    state.savingNotes -> {
                        Log.d("NotesState", "lOAING..")
                    }

                    state.notesSaved == "Notes Saved!" -> {
                        Log.d("NotesState", "sAVED..")
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp),
            containerColor = colorResource(id = R.color.medium1_blue)
        ) {
            Icon(
                Icons.Filled.Done,
                contentDescription = "Add",
                tint = colorResource(id = R.color.white)
            )
        }
    }

    LaunchedEffect(state) {
        when{
            state.savingNotes ->{

            }
            state.notesSaved == "Notes Saved!" -> {
                navController.navigate(HomeScreen) {
                    popUpTo(HomeScreen) { inclusive = true } //removes circular navigation
                }
            }
            state.notesError == "Notes Not Saved!" ->{

            }
        }
    }
}