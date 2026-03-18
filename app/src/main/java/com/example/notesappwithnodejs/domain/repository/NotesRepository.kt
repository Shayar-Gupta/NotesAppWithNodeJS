package com.example.notesappwithnodejs.domain.repository

import com.example.notesappwithnodejs.components.utils.Resource
import com.example.notesappwithnodejs.domain.models.Notes
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun saveNotes(notes: Notes): Flow<Resource<String>>
    suspend fun getNotes(): Flow<Resource<List<Notes>>>
    suspend fun updateNote(id: String, notes: Notes): Flow<Resource<String>>
    suspend fun deleteNote(id: String): Flow<Resource<String>>
}