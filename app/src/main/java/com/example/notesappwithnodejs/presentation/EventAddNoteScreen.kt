package com.example.notesappwithnodejs.presentation

import com.example.notesappwithnodejs.domain.models.Notes

sealed class EventAddNoteScreen {
    data class NoteTitle(val title: String) : EventAddNoteScreen()
    data class NoteDescription(val description: String) : EventAddNoteScreen()
    data class NotePriority(val priority: String) : EventAddNoteScreen()
    data class SaveNote(val notes: Notes) : EventAddNoteScreen()
    data class UpdateNote(val id: String, val notes: Notes) : EventAddNoteScreen()
    data class LoadNote(                      // carries plain fields — no Notes object needed
        val id: String,
        val title: String,
        val description: String,
        val priority: String
    ) : EventAddNoteScreen()
}