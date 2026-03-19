package com.example.notesappwithnodejs.data.remote

import com.example.notesappwithnodejs.domain.models.Notes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesAPI {
    @POST("/save-notes")
    suspend fun saveNotes(@Body notes: Notes): Response<Notes>

    @GET("/notes")
    suspend fun getNotes(): Response<List<Notes>>

    @GET("/notes/{id}")
    suspend fun getNoteById(@Path("id") id: String): Response<Notes>

    @PUT("/notes/{id}")
    suspend fun updateNote(@Path("id") id: String, @Body notes: Notes): Response<Notes>

    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Path("id") id: String): Response<Notes>
}