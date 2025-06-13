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

@Composable
fun HealthDetailPage() {
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
        config = configDao.get()!!
        diseaseInfo = config.diseaseInfo
        historyInfo0 = config.historyInfo0
        historyInfo1 = config.historyInfo1
        historyInfo2 = config.historyInfo2
    }
    LaunchedEffect(Unit) {
        healthInfo = healthInfoDao.getLatest()
    }
    LaunchedEffect(shouldUpdate) {
        if (shouldUpdate) {
            configDao.update(config)
            shouldUpdate = false
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
                HealthHeader(config)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("基本信息", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("体力状况评分：0分（优秀）")
                        Text("身高体重：${config.height}cm ${config.weight}kg")
                        Text("血压：${healthInfo?.systolicBloodPressure ?: 0.0} /${healthInfo?.diastolicBloodPressure ?: 0.0} mmHg")
                        Text("心率：${healthInfo?.heartRate ?: 0} bpm")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
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
                            Text(diseaseInfo)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("既往史: $historyInfo0")
                            Text("个人史: $historyInfo1")
                            Text("家庭史: $historyInfo2")
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