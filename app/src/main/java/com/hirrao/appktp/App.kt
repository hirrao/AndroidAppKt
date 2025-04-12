package com.hirrao.appktp

import android.app.Application
import androidx.room.Room
import com.hirrao.appktp.data.AppDataBase

class App : Application() {
    companion object {
        lateinit var dataBase: AppDataBase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        dataBase = Room.databaseBuilder(
            applicationContext, AppDataBase::class.java, "app_database"
        ).build()
    }
}