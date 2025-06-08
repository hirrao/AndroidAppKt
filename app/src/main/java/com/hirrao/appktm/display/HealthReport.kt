package com.hirrao.appktm.display

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HealthAppScreen() {
    var currentPage by remember { mutableStateOf(0) }

    val tabTitles = listOf("个人概览", "病情概况", "用药管理")

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0288D1)),
            horizontalArrangement = Arrangement.Center
        ) {
            TabItem(
                text = tabTitles[0],
                isSelected = currentPage == 0,
                onClick = { currentPage = 0 },
            )
            TabItem(
                text = tabTitles[1],
                isSelected = currentPage == 1,
                onClick = { currentPage = 1 },
            )
            TabItem(
                text = tabTitles[2],
                isSelected = currentPage == 2,
                onClick = { currentPage = 2 },
            )
        }
        when (currentPage) {
            0 -> HealthOverviewPage()
            1 -> HealthDetailPage()
            2 -> MedicineManagePage()
        }

    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .clickable { onClick() }
        .background(if (isSelected) Color(0xFF0277BD) else Color(0xFF0288D1))
        .padding(vertical = 12.dp, horizontal = 16.dp), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            letterSpacing = 2.sp
        )
    }
}

@Composable
fun HealthHeader() {
    Text("云霄    男 | 2004-07-10", fontSize = 16.sp)
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
@Preview
fun HealthOverviewPage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp)
    ) {
        item {
            HealthHeader()
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("2024-08-26 18:34", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("收缩压\n155 mmHg", textAlign = TextAlign.Center)
                        Text("舒张压\n99 mmHg", textAlign = TextAlign.Center)
                        Text("心率\n89 次/分钟", textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("风险评估：轻度", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("最近七天血压异常统计", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("异常血压统计：轻度 1次，其他 0次")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("2024-08-26 用药管理", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("阿莫西林 00:00 1毫克")
                    Text("四季感冒片 00:00 20毫克")
                    Text("四季感冒片 12:00 20毫克")
                }
            }
        }
    }
}

@Composable
@Preview
fun HealthDetailPage() {
    var isEditing by remember { mutableStateOf(false) }
    var diseaseInfo by remember { mutableStateOf("高血压，病程1年2月") }
    var historyInfo by remember { mutableStateOf("既往史：无记录\n个人史：无记录\n家族史：无记录") }
    var tempDiseaseInfo by remember { mutableStateOf(diseaseInfo) }
    var tempHistoryInfo by remember { mutableStateOf(historyInfo) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp)
    ) {
        item {
            HealthHeader()
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("基本信息", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("体力状况评分：0分（优秀）")
                    Text("身高体重：172cm 95.5kg")
                    Text("血压：155/99 mmHg")
                    Text("心率：89 bpm")
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
                        androidx.compose.material3.OutlinedTextField(
                            value = tempDiseaseInfo,
                            onValueChange = { tempDiseaseInfo = it },
                            label = { Text("疾病信息") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.OutlinedTextField(
                            value = tempHistoryInfo,
                            onValueChange = { tempHistoryInfo = it },
                            label = { Text("病史信息") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(tempDiseaseInfo)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(tempHistoryInfo)
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(80.dp)) // 给底部按钮留空间
        }
    }
    // 底部编辑/保存按钮
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        if (isEditing) {
            Row(modifier = Modifier.padding(16.dp)) {
                Button(onClick = {
                    isEditing = false
                    tempDiseaseInfo = diseaseInfo
                    tempHistoryInfo = historyInfo
                }) {
                    Text("取消")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    isEditing = false
                    diseaseInfo = tempDiseaseInfo
                    historyInfo = tempHistoryInfo
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

@Composable
@Preview
fun MedicineManagePage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp)
    ) {
        item {
            HealthHeader()
            Text("用药管理", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            // 这里可以硬编码一些用药信息
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("阿莫西林 00:00 1毫克")
                    Text("四季感冒片 00:00 20毫克")
                    Text("四季感冒片 12:00 20毫克")
                }
            }
        }
    }
}
