package com.example.notesappwithnodejs.base

import android.app.Application
import com.example.notesappwithnodejs.data.di.dataModules
import com.example.notesappwithnodejs.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules (presentationModule, dataModules)
            androidContext(this@NotesApplication)
        }
    }
}