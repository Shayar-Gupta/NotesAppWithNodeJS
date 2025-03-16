package com.example.notesappwithnodejs.presentation.di

import com.example.notesappwithnodejs.presentation.ViewModelAddNoteScreen
import com.example.notesappwithnodejs.presentation.ViewModelHomeScreen
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        ViewModelAddNoteScreen()
    }

    viewModel {
        ViewModelHomeScreen()
    }
}