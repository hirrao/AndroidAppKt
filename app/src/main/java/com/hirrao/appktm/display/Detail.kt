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

@Composable
fun HealthDetailPage() {
    val configDao = App.configDao
    var config by remember { mutableStateOf<Config>(Config()) }
    LaunchedEffect(Unit) {
        config = configDao.get()!!
    }
    var shouldUpdate by remember { mutableStateOf(false) }
    LaunchedEffect(shouldUpdate) {
        if (shouldUpdate) {
            configDao.update(config)
            shouldUpdate = false
        }
    }
    var isEditing by remember { mutableStateOf(false) }
    var diseaseInfo by remember { mutableStateOf(config.diseaseInfo) }
    var historyInfo0 by remember { mutableStateOf(config.historyInfo0) }
    var historyInfo1 by remember { mutableStateOf(config.historyInfo1) }
    var historyInfo2 by remember { mutableStateOf(config.historyInfo2) }
    var tempDiseaseInfo by remember { mutableStateOf(diseaseInfo) }
    var tempHistoryInfo0 by remember { mutableStateOf(historyInfo0) }
    var tempHistoryInfo1 by remember { mutableStateOf(historyInfo1) }
    var tempHistoryInfo2 by remember { mutableStateOf(historyInfo2) }
    var healthInfo by remember { mutableStateOf<HealthInfo?>(null) }
    LaunchedEffect(Unit) {
        healthInfo = healthInfoDao.getLatest()
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
                                value = tempDiseaseInfo,
                                onValueChange = { tempDiseaseInfo = it },
                                label = { Text("疾病信息") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = tempHistoryInfo0,
                                onValueChange = { tempHistoryInfo0 = it },
                                label = { Text("既往史") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = tempHistoryInfo1,
                                onValueChange = { tempHistoryInfo1 = it },
                                label = { Text("个人史") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = tempHistoryInfo1,
                                onValueChange = { tempHistoryInfo1 = it },
                                label = { Text("家庭史") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(tempDiseaseInfo)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("既往史: $tempHistoryInfo0")
                            Text("个人史: $tempHistoryInfo1")
                            Text("家庭史: $tempHistoryInfo2")
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
                            diseaseInfo = tempDiseaseInfo,
                            historyInfo0 = tempHistoryInfo0,
                            historyInfo1 = tempHistoryInfo1,
                            historyInfo2 = tempHistoryInfo2
                        )
                        shouldUpdate = true
                        CoroutineScope(Dispatchers.IO).launch {
                            configDao.update(config)
                            shouldUpdate = false
                        }
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