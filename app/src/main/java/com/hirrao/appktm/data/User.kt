package com.hirrao.appktm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int,
    val height: Double,
    val weight: Double,
    val systolicBloodPressure: Int,
    val diastolicBloodPressure: Int,
    val heartRate: Int,
)