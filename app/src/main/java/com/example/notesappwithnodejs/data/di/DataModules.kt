package com.example.notesappwithnodejs.data.di

import com.example.notesappwithnodejs.data.remote.NotesAPI
import com.example.notesappwithnodejs.data.repository.NotesRepositoryImplement
import com.example.notesappwithnodejs.domain.repository.NotesRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideNotesApi(): NotesAPI {
    return Retrofit.Builder().baseUrl("http://192.168.43.236:3000")
        .addConverterFactory(GsonConverterFactory.create()).build().create(NotesAPI::class.java)
}

fun provideNotesRepository(notesAPI: NotesAPI) : NotesRepository{
    return NotesRepositoryImplement(notesAPI)
}

val dataModules = module {
    single {
        provideNotesApi()
    }
    single {
        provideNotesRepository(get())
    }

}