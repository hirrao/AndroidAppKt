package com.hirrao.appktm

import android.app.Application
import androidx.room.Room
import com.hirrao.appktm.data.AppDataBase
import com.hirrao.appktm.data.Config
import com.hirrao.appktm.data.ConfigDao
import com.hirrao.appktm.data.HealthInfoDao
import com.hirrao.appktm.data.MachineInfoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class App : Application() {
    companion object {
        lateinit var dataBase: AppDataBase
            private set
        lateinit var configDao: ConfigDao
        lateinit var machineInfoDao: MachineInfoDao
        lateinit var healthInfoDao: HealthInfoDao
    }

    override fun onCreate() {
        super.onCreate()
        dataBase = Room.databaseBuilder(
                applicationContext, AppDataBase::class.java, "app_database"
            ).fallbackToDestructiveMigration(true).build()
        configDao = dataBase.ConfigDao()
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            configDao.get() ?: configDao.insert(Config())
        }
        machineInfoDao = dataBase.MachineInfoDao()
        healthInfoDao = dataBase.HealthInfoDao()
    }
}