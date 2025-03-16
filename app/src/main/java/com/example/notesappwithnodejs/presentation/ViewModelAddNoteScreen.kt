package com.example.notesappwithnodejs.presentation

import androidx.compose.ui.util.fastCbrt
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

    private val notesRepository : NotesRepository by inject()

    private val _stateNote = MutableStateFlow(StateAddNoteScreen())
    val notesState = _stateNote

    private fun saveNotes(notes : Notes){
        viewModelScope.launch {
            notesRepository.saveNotes(notes).collect { resource ->
                when(resource){
                    is Resource.Loading -> {
                        _stateNote.value = notesState.value.copy(savingNotes = true)
                    }
                    is Resource.Success -> {
                        _stateNote.value = notesState.value.copy(notesSaved = resource.data.toString(), savingNotes = false)
                    }
                    is Resource.Error -> {
                        _stateNote.value = notesState.value.copy(notesError = resource.message.toString(), savingNotes = false)
                    }
                }
            }

        }
    }

    fun onEvent(event: EventAddNoteScreen){
        when(event){
            is EventAddNoteScreen.NoteTitle -> {
                _stateNote.value = notesState.value.copy(notesTitle = event.title)
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
        }
    }
}

