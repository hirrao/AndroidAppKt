package com.hirrao.appktm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Config::class, MachineInfo::class, HealthInfo::class], version = 5)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun ConfigDao(): ConfigDao
    abstract fun MachineInfoDao(): MachineInfoDao
    abstract fun HealthInfoDao(): HealthInfoDao
}