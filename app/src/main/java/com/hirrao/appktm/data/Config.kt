package com.hirrao.appktm.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Entity(tableName = "User_Config")
data class Config(
    @PrimaryKey val id: Int = 0,
    val userName: String = "没有用户名",
    val sex: String = "男",
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val birthday: LocalDate = LocalDate.of(2004, 1, 1),
    val historyInfo0: String = "无记录",
    val historyInfo1: String = "无记录",
    val historyInfo2: String = "无记录",
    val diseaseInfo: String = "无",
    val isFirstLaunch: Boolean = true
)

@Entity(tableName = "Health_Info")
data class HealthInfo(
    @PrimaryKey val id: Int = createId(),
    val recordTime: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
    val systolicBloodPressure: Int = 0,
    val diastolicBloodPressure: Int = 0,
    val heartRate: Int = 0,
) {
    companion object {
        var lastId: Int = 0
        fun createId(): Int {
            val newId = (System.currentTimeMillis() / 1000L).toInt()
            if (newId > lastId) {
                lastId = newId
                return newId
            } else throw Exception("1")
        }
    }
}

@Entity(tableName = "Machine_Info")
data class MachineInfo(
    @PrimaryKey val id: Int = 0,
    val machineName: String = "",
    val eatTime: LocalTime = LocalTime.of(0, 0),
    val dosage: Double, // 单位毫克
)