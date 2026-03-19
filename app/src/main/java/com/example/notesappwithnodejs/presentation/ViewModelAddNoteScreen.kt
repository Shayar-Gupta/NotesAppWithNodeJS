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

class ViewModelAddNoteScreen : ViewModel(), KoinComponent {

    private val notesRepository: NotesRepository by inject()

    private val _stateNote = MutableStateFlow(StateAddNoteScreen())
    val notesState = _stateNote

    private fun validate(): Boolean {
        if (_stateNote.value.notesTitle.isBlank()) {
            _stateNote.value = _stateNote.value.copy(titleError = "Title cannot be empty")
            return false
        }
        _stateNote.value = _stateNote.value.copy(titleError = "")
        return true
    }

    private fun saveNotes(notes: Notes) {
        if (!validate()) return
        viewModelScope.launch {
            notesRepository.saveNotes(notes).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _stateNote.value = notesState.value.copy(savingNotes = true)
                    is Resource.Success -> _stateNote.value = notesState.value.copy(
                        notesSaved = "Notes Saved!",
                        savingNotes = false
                    )
                    is Resource.Error -> _stateNote.value = notesState.value.copy(
                        notesError = resource.message.toString(),
                        savingNotes = false
                    )
                }
            }
        }
    }

    private fun updateNote(id: String, notes: Notes) {
        if (!validate()) return
        viewModelScope.launch {
            notesRepository.updateNote(id, notes).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _stateNote.value = notesState.value.copy(savingNotes = true)
                    is Resource.Success -> _stateNote.value = notesState.value.copy(
                        notesSaved = "Note Updated!",
                        savingNotes = false
                    )
                    is Resource.Error -> _stateNote.value = notesState.value.copy(
                        notesError = resource.message.toString(),
                        savingNotes = false
                    )
                }
            }
        }
    }

    fun onEvent(event: EventAddNoteScreen) {
        when (event) {
            is EventAddNoteScreen.NoteTitle -> {
                _stateNote.value = notesState.value.copy(
                    notesTitle = event.title,
                    titleError = ""
                )
            }
            is EventAddNoteScreen.NoteDescription -> {
                _stateNote.value = notesState.value.copy(notesDescription = event.description)
            }
            is EventAddNoteScreen.NotePriority -> {
                _stateNote.value = notesState.value.copy(notesPriority = event.priority)
            }
            is EventAddNoteScreen.SaveNote -> {
                saveNotes(event.notes)
            }
            is EventAddNoteScreen.UpdateNote -> {
                updateNote(event.id, event.notes)
            }
            is EventAddNoteScreen.LoadNote -> {
                // directly write all fields into state — UI recomposes immediately
                _stateNote.value = notesState.value.copy(
                    isEditMode = true,
                    editingNoteId = event.id,
                    notesTitle = event.title,
                    notesDescription = event.description,
                    notesPriority = event.priority,
                    titleError = ""
                )
            }
        }
    }
}