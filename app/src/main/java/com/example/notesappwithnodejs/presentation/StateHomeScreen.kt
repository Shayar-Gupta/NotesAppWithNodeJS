package com.example.notesappwithnodejs.presentation

import com.example.notesappwithnodejs.domain.models.Notes

data class StateHomeScreen(
    val gettingNotes : Boolean = false,
    val fetchedNotes : List<Notes> ?= emptyList(),
    val error : String = ""
)