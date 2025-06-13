package com.hirrao.appktm

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirrao.appktm.data.AppDataBase
import com.hirrao.appktm.data.Config
import com.hirrao.appktm.data.ConfigDao
import com.hirrao.appktm.data.HealthInfo
import com.hirrao.appktm.data.HealthInfoDao
import com.hirrao.appktm.data.MachineInfo
import com.hirrao.appktm.data.MachineInfoDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConfigDataBaseTest {
    private lateinit var db: AppDataBase
    private lateinit var configDao: ConfigDao
    private lateinit var machineInfoDao: MachineInfoDao
    private lateinit var healthInfoDao: HealthInfoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), AppDataBase::class.java
        ).build()
        configDao = db.ConfigDao()
        machineInfoDao = db.MachineInfoDao()
        healthInfoDao = db.HealthInfoDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetConfig() = runBlocking {
        val config = Config(userName = "testUser", sex = "女", isFirstLaunch = false)
        configDao.insert(config)
        val loaded = configDao.get()
        assertNotNull(loaded)
        assertEquals("testUser", loaded?.userName)
        assertEquals("女", loaded?.sex)
        assertFalse(loaded?.isFirstLaunch != false)
    }

    @Test
    fun updateConfig() = runBlocking {
        val config = Config(userName = "origin", sex = "男")
        configDao.insert(config)
        val updated = config.copy(userName = "updated", sex = "女")
        configDao.update(updated)
        val loaded = configDao.get()
        assertEquals("updated", loaded?.userName)
        assertEquals("女", loaded?.sex)
    }

    @Test
    fun insertAndGetMachineInfo() = runBlocking {
        val machineInfo = MachineInfo(machineName = "TestMachine", dosage = 10.0)
        machineInfoDao.insert(machineInfo)
        val all = machineInfoDao.getAll()
        assertTrue(all.isNotEmpty())
        val loaded = machineInfoDao.getById(machineInfo.id)
        assertNotNull(loaded)
        assertEquals("TestMachine", loaded?.machineName)
        assertEquals(10.0, loaded!!.dosage, 0.01)
    }

    @Test
    fun updateAndDeleteMachineInfo() = runBlocking {
        val machineInfo = MachineInfo(machineName = "TestMachine", dosage = 10.0)
        machineInfoDao.insert(machineInfo)
        val updated = machineInfo.copy(machineName = "UpdatedMachine", dosage = 20.0)
        machineInfoDao.update(updated)
        val loaded = machineInfoDao.getById(machineInfo.id)
        assertEquals("UpdatedMachine", loaded?.machineName)
        assertEquals(20.0, loaded!!.dosage, 0.01)
        machineInfoDao.delete(updated)
        val afterDelete = machineInfoDao.getById(machineInfo.id)
        assertNull(afterDelete)
    }

    @Test
    fun insertAndGetHealthInfo() = runBlocking {
        val healthInfo = HealthInfo(
            height = 170.0,
            weight = 60.0,
            systolicBloodPressure = 120.0,
            diastolicBloodPressure = 80.0,
            heartRate = 70
        )
        healthInfoDao.insert(healthInfo)
        val all = healthInfoDao.getAll()
        assertTrue(all.isNotEmpty())
        val latest = healthInfoDao.getLatest()
        assertNotNull(latest)
        assertEquals(170.0, latest!!.height, 0.01)
        assertEquals(60.0, latest.weight, 0.01)
    }

    @Test
    fun updateHealthInfo() = runBlocking {
        val healthInfo = HealthInfo(
            height = 170.0,
            weight = 60.0,
            systolicBloodPressure = 120.0,
            diastolicBloodPressure = 80.0,
            heartRate = 70
        )
        healthInfoDao.insert(healthInfo)
        val updated = healthInfo.copy(height = 180.0, weight = 65.0)
        healthInfoDao.update(updated)
        val latest = healthInfoDao.getLatest()
        assertEquals(180.0, latest!!.height, 0.01)
        assertEquals(65.0, latest.weight, 0.01)
    }
}