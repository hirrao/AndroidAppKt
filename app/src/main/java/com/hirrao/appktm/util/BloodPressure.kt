package com.hirrao.appktm.util

fun generateRiskLevel(sbp: Double, dbp: Double): String {
    return when {
        sbp >= 180 || dbp >= 110 -> "重度"
        sbp >= 160 || dbp >= 100 -> "中度"
        sbp >= 140 || dbp >= 90 -> "轻度"
        sbp >= 120 || dbp >= 80 -> "正常高值"
        sbp <= 90 || dbp <= 60 -> "偏低"
        else -> "正常"
    }
}