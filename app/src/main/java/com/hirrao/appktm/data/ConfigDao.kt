package com.hirrao.appktm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ConfigDao {
    @Query("SELECT * FROM User_Config WHERE id = 0")
    suspend fun get(): Config?

    @Insert
    suspend fun insert(config: Config)

    @Update
    suspend fun update(config: Config)
}

@Dao
interface MachineInfoDao {
    @Query("SELECT * FROM Machine_Info")
    suspend fun getAll(): List<MachineInfo>

    @Query("SELECT * FROM Machine_Info WHERE id = :id")
    suspend fun getById(id: Int): MachineInfo?

    @Insert
    suspend fun insert(machineInfo: MachineInfo)

    @Update
    suspend fun update(machineInfo: MachineInfo)

    @Delete
    suspend fun delete(machineInfo: MachineInfo)
}

@Dao
interface HealthInfoDao {
    @Query("SELECT * FROM Health_Info")
    suspend fun getAll(): List<HealthInfo>

    @Query("SELECT * FROM Health_Info WHERE id = (SELECT MAX(id) FROM Health_Info)")
    suspend fun getLatest(): HealthInfo?

    @Insert
    suspend fun insert(healthInfo: HealthInfo)

    @Update
    suspend fun update(healthInfo: HealthInfo)
}