package com.example.notesappwithnodejs.presentation

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AddNotesScreen(
    val noteId: String? = null,
    val noteTitle: String? = null,
    val noteDescription: String? = null,
    val notePriority: String? = null,
)