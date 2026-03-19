package com.example.notesappwithnodejs.presentation

data class StateAddNoteScreen(
    val isEditMode: Boolean = false,
    val editingNoteId: String? = null,
    val savingNotes : Boolean = false,
    val notesSaved : String = "",
    val notesError : String = "",
    val notesTitle : String = "",
    val notesDescription : String = "",
    val notesPriority : String = "Low",
    val titleError: String = "",
)