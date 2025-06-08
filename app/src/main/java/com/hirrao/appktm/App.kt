package com.hirrao.appktm

import android.app.Application
import androidx.room.Room
import com.hirrao.appktm.data.AppDataBase
import com.hirrao.appktm.data.UserDao

class App : Application() {
    companion object {
        lateinit var dataBase: AppDataBase
            private set
        lateinit var userDao: UserDao
    }

    override fun onCreate() {
        super.onCreate()
        dataBase = Room.databaseBuilder(
            applicationContext, AppDataBase::class.java, "app_database"
        ).build()
        userDao = dataBase.UserDao()
    }
}