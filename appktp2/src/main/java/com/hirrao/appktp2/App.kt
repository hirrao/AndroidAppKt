package com.hirrao.appktp2

import android.app.Application
import android.content.ContentResolver

class App : Application() {
    companion object {
        lateinit var contentResolver: ContentResolver
    }

    override fun onCreate() {
        Companion.contentResolver = contentResolver
        super.onCreate()
    }
}