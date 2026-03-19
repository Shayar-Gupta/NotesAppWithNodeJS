package com.example.notesappwithnodejs.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappwithnodejs.components.utils.Resource
import com.example.notesappwithnodejs.domain.models.Notes
import com.example.notesappwithnodejs.domain.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ViewModelHomeScreen : ViewModel(), KoinComponent {

    private val notesRepository: NotesRepository by inject()
    private val _getNotesState = MutableStateFlow(StateHomeScreen())
    val getNotesState = _getNotesState

    init {
        getNotes()
    }

    fun getNotes() {
        viewModelScope.launch {
            notesRepository.getNotes().collect { resource ->
                when (resource) {
                    is Resource.Success -> _getNotesState.value = _getNotesState.value.copy(
                        fetchedNotes = resource.data,
                        gettingNotes = false
                    )
                    is Resource.Loading -> _getNotesState.value = _getNotesState.value.copy(
                        gettingNotes = true
                    )
                    is Resource.Error -> _getNotesState.value = _getNotesState.value.copy(
                        error = resource.message ?: "",
                        gettingNotes = false
                    )
                }
            }
        }
    }

    // called from both swipe AND long-press → delete menu
    fun confirmDelete(note: Notes) {
        _getNotesState.value = _getNotesState.value.copy(showDeleteConfirmFor = note)
    }

    fun dismissDelete() {
        _getNotesState.value = _getNotesState.value.copy(showDeleteConfirmFor = null)
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            notesRepository.deleteNote(id).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _getNotesState.value = _getNotesState.value.copy(
                        deletingNoteId = id
                    )
                    is Resource.Success -> {
                        _getNotesState.value = _getNotesState.value.copy(
                            deletingNoteId = null,
                            showDeleteConfirmFor = null,
                            deleteSuccess = true      // HomeScreen LaunchedEffect picks this up
                        )
                        getNotes()                    // refresh list
                    }
                    is Resource.Error -> _getNotesState.value = _getNotesState.value.copy(
                        deletingNoteId = null
                    )
                }
            }
        }
    }

    // called by HomeScreen after the Snackbar has been shown
    fun clearDeleteSuccess() {
        _getNotesState.value = _getNotesState.value.copy(deleteSuccess = false)
    }
}