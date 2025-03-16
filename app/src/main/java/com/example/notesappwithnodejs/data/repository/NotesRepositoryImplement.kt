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

        val status = notesAPI.saveNotes(notes).code()

        try {
            if (status == 201) emit(Resource.Success("Notes Saved!"))
            else emit(Resource.Error("Notes Not Saved!"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message!!))
        }
    }

    override suspend fun getNotes(): Flow<Resource<List<Notes>>> = flow {
        val status = notesAPI.getNotes().code()

        try {
            val notesList = notesAPI.getNotes().body()
            if (status == 200) emit(Resource.Success(notesList))
            else emit(Resource.Error("Notes Not Saved!"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message!!))
        }
    }
}