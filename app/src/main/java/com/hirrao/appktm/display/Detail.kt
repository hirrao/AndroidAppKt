package com.hirrao.appktm.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hirrao.appktm.App
import com.hirrao.appktm.App.Companion.healthInfoDao
import com.hirrao.appktm.data.Config
import com.hirrao.appktm.data.HealthInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HealthDetailPage(
    headerUser: Config
) {
    val configDao = App.configDao
    var config by remember { mutableStateOf<Config>(Config()) }
    var shouldUpdate by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var diseaseInfo by remember { mutableStateOf(config.diseaseInfo) }
    var historyInfo0 by remember { mutableStateOf(config.historyInfo0) }
    var historyInfo1 by remember { mutableStateOf(config.historyInfo1) }
    var historyInfo2 by remember { mutableStateOf(config.historyInfo2) }
    var healthInfo by remember { mutableStateOf<HealthInfo?>(null) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            config = configDao.get()!!
            diseaseInfo = config.diseaseInfo
            historyInfo0 = config.historyInfo0
            historyInfo1 = config.historyInfo1
            historyInfo2 = config.historyInfo2
        }
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            healthInfo = healthInfoDao.getLatest()
        }
    }
    LaunchedEffect(shouldUpdate) {
        if (shouldUpdate) {
            withContext(Dispatchers.IO) {
                configDao.update(config)
                shouldUpdate = false
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFB3E5FC))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                HealthHeader(headerUser)
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    BasicDetail(healthInfo = healthInfo)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("疾病信息：", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        if (isEditing) {
                            OutlinedTextField(
                                value = diseaseInfo,
                                onValueChange = { diseaseInfo = it },
                                label = { Text("疾病信息") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = historyInfo0,
                                onValueChange = { historyInfo0 = it },
                                label = { Text("既往史") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = historyInfo1,
                                onValueChange = { historyInfo1 = it },
                                label = { Text("个人史") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = historyInfo2,
                                onValueChange = { historyInfo2 = it },
                                label = { Text("家庭史") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(if (diseaseInfo.isNotBlank()) diseaseInfo else "无疾病信息")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("既往史: ${if (historyInfo0.isNotBlank()) historyInfo0 else "无信息"}")
                            Text("个人史: ${if (historyInfo1.isNotBlank()) historyInfo1 else "无信息"}")
                            Text("家庭史: ${if (historyInfo2.isNotBlank()) historyInfo2 else "无信息"}")
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            if (isEditing) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Button(onClick = {
                        isEditing = false
                    }) {
                        Text("取消")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        isEditing = false
                        config = config.copy(
                            diseaseInfo = diseaseInfo,
                            historyInfo0 = historyInfo0,
                            historyInfo1 = historyInfo1,
                            historyInfo2 = historyInfo2
                        )
                        shouldUpdate = true
                    }) {
                        Text("保存")
                    }
                }
            } else {
                Button(
                    onClick = { isEditing = true }, modifier = Modifier.padding(16.dp)
                ) {
                    Text("编辑")
                }
            }
        }
    }
}

@Composable
fun BasicDetail(healthInfo: HealthInfo?) {
    var isAdding by remember { mutableStateOf(false) }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var systolic by remember { mutableStateOf("") }
    var diastolic by remember { mutableStateOf("") }
    var heartRate by remember { mutableStateOf("") }
    val heightValid = height.isNotBlank() && height.toDoubleOrNull() != null
    val weightValid = weight.isNotBlank() && weight.toDoubleOrNull() != null
    val systolicValid = systolic.isNotBlank() && systolic.toDoubleOrNull() != null
    val diastolicValid = diastolic.isNotBlank() && diastolic.toDoubleOrNull() != null
    val heartRateValid = heartRate.isNotBlank() && heartRate.toIntOrNull() != null
    val allFilled = heightValid && weightValid && systolicValid && diastolicValid && heartRateValid
    Column(modifier = Modifier.padding(16.dp)) {
        Text("基本信息", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        if (isAdding) {
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("身高(cm)") },
                modifier = Modifier.fillMaxWidth(),
                isError = !heightValid
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("体重(kg)") },
                modifier = Modifier.fillMaxWidth(),
                isError = !weightValid
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = systolic,
                onValueChange = { systolic = it },
                label = { Text("收缩压(mmHg)") },
                modifier = Modifier.fillMaxWidth(),
                isError = !systolicValid
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = diastolic,
                onValueChange = { diastolic = it },
                label = { Text("舒张压(mmHg)") },
                modifier = Modifier.fillMaxWidth(),
                isError = !diastolicValid
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = heartRate,
                onValueChange = { heartRate = it },
                label = { Text("心率(bpm)") },
                modifier = Modifier.fillMaxWidth(),
                isError = !heartRateValid
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = { isAdding = false }, modifier = Modifier.weight(1f)) {
                    Text("取消")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            healthInfoDao.insert(
                                HealthInfo(
                                    height = height.toDouble(),
                                    weight = weight.toDouble(),
                                    systolicBloodPressure = systolic.toDouble(),
                                    diastolicBloodPressure = diastolic.toDouble(),
                                    heartRate = heartRate.toInt()
                                )
                            )
                        }
                        isAdding = false
                    }, modifier = Modifier.weight(1f), enabled = allFilled
                ) {
                    Text("保存")
                }
            }
        } else {
            Text("身高体重：${healthInfo?.height ?: 0.0}cm ${healthInfo?.weight ?: 0.0}kg")
            Text("血压：${healthInfo?.systolicBloodPressure ?: 0.0} /${healthInfo?.diastolicBloodPressure ?: 0.0} mmHg")
            Text("心率：${healthInfo?.heartRate ?: 0} bpm")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { isAdding = true }) {
                Text("插入信息")
            }
        }
    }
}