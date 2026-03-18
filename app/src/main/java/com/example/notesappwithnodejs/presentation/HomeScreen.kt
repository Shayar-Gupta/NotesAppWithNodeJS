package com.example.notesappwithnodejs.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notesappwithnodejs.R
import com.example.notesappwithnodejs.components.AppToolBar
import com.example.notesappwithnodejs.domain.models.NotesCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    state: StateHomeScreen,
    viewModel: ViewModelHomeScreen = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // show snackbar after a successful delete
    LaunchedEffect(state.deleteSuccess) {
        if (state.deleteSuccess) {
            snackbarHostState.showSnackbar("Note deleted")
            viewModel.clearDeleteSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppToolBar(title = "Notes")

            // ── Loading spinner ───────────────────────────────────────────────
            if (state.gettingNotes) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.medium1_blue)
                    )
                }
            }

            // ── Empty state ───────────────────────────────────────────────────
            if (state.fetchedNotes.isNullOrEmpty() && !state.gettingNotes) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(Modifier.height(12.dp))
                        Text("No notes yet. Tap + to add one.", color = Color.Gray)
                    }
                }
            }

            // ── Notes grid ────────────────────────────────────────────────────
            if (isNotesFetched(state)) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp
                ) {
                    items(
                        items = state.fetchedNotes!!,
                        key = { it._id ?: it.hashCode() }   // stable keys = smooth animations
                    ) { note ->
                        var showMenu by remember { mutableStateOf(false) }

                        // ── Swipe-to-delete (swipe left) ──────────────────────
                        val swipeState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.confirmDelete(note)
                                }
                                // snap back — real delete happens after AlertDialog confirm
                                false
                            }
                        )

                        SwipeToDismissBox(
                            state = swipeState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val bgColor by animateColorAsState(
                                    targetValue = if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart)
                                        Color(0xFFE53935) else Color.Transparent,
                                    label = "swipe_bg"
                                )
                                val iconScale by animateFloatAsState(
                                    targetValue = if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart)
                                        1f else 0.75f,
                                    label = "swipe_icon_scale"
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(bgColor, RoundedCornerShape(8.dp))
                                        .padding(end = 16.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier.scale(iconScale)
                                    )
                                }
                            }
                        ) {
                            // card + long-press dropdown
                            Box {
                                NotesCard(
                                    notes = note,
                                    modifier = Modifier.combinedClickable(
                                        onClick = {},
                                        onLongClick = { showMenu = true }
                                    )
                                )
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Edit") },
                                        leadingIcon = { Icon(Icons.Default.Edit, null) },
                                        onClick = {
                                            showMenu = false
                                            navController.navigate(
                                                AddNotesScreen(
                                                    noteId = note._id,
                                                    noteTitle = note.notesTitle,
                                                    noteDescription = note.noteDescription,
                                                    notePriority = note.notesPriority
                                                )
                                            )
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Delete", color = Color.Red) },
                                        leadingIcon = {
                                            Icon(Icons.Default.Delete, null, tint = Color.Red)
                                        },
                                        onClick = {
                                            showMenu = false
                                            viewModel.confirmDelete(note)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Delete confirmation dialog ─────────────────────────────────────────
        state.showDeleteConfirmFor?.let { note ->
            AlertDialog(
                onDismissRequest = { viewModel.dismissDelete() },
                title = { Text("Delete note?") },
                text = { Text("\"${note.notesTitle}\" will be permanently deleted.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.deleteNote(note._id!!) }) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDelete() }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // ── Snackbar ───────────────────────────────────────────────────────────
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // ── FAB ───────────────────────────────────────────────────────────────
        FloatingActionButton(
            onClick = { navController.navigate(AddNotesScreen()) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp),
            containerColor = colorResource(id = R.color.medium1_blue)
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add",
                tint = colorResource(id = R.color.white)
            )
        }
    }
}

fun isNotesFetched(state: StateHomeScreen): Boolean {
    return when {
        state.gettingNotes -> false
        state.fetchedNotes.isNullOrEmpty() -> false
        else -> true
    }
}