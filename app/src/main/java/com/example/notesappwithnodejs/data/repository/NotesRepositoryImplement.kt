package com.example.notesappwithnodejs.data.repository

import com.example.notesappwithnodejs.components.utils.Resource
import com.example.notesappwithnodejs.data.remote.NotesAPI
import com.example.notesappwithnodejs.domain.models.Notes
import com.example.notesappwithnodejs.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotesRepositoryImplement(val notesAPI: NotesAPI) : NotesRepository {
    override suspend fun saveNotes(notes: Notes): Flow<Resource<String>> = flow {
        emit(Resource.Loading())


        try {
            val status = notesAPI.saveNotes(notes).code()
            if (status == 201) emit(Resource.Success("Notes Saved!"))
            else emit(Resource.Error("Notes Not Saved!"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message!!))
        }
    }

    override suspend fun getNotes(): Flow<Resource<List<Notes>>> = flow {
        emit(Resource.Loading())
        try {
            val response = notesAPI.getNotes()
            if (response.isSuccessful) emit(Resource.Success(response.body()))
            else emit(Resource.Error("Failed to fetch notes"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun updateNote(id: String, notes: Notes): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = notesAPI.updateNote(id, notes)
            if (response.isSuccessful) emit(Resource.Success("Note Updated!"))
            else emit(Resource.Error("Update failed"))
        } catch (e: Exception) { emit(Resource.Error(e.message ?: "Error")) }
    }

    override suspend fun deleteNote(id: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = notesAPI.deleteNote(id)
            if (response.isSuccessful) emit(Resource.Success("Note Deleted!"))
            else emit(Resource.Error("Delete failed"))
        } catch (e: Exception) { emit(Resource.Error(e.message ?: "Error")) }
    }
}