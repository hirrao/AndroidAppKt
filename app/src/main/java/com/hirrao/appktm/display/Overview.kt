package com.hirrao.appktm.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hirrao.appktm.App.Companion.healthInfoDao
import com.hirrao.appktm.data.Config
import com.hirrao.appktm.data.HealthInfo
import java.time.format.DateTimeFormatter

@Composable
fun HealthOverviewPage(user: Config) {
    var healthInfo by remember { mutableStateOf<HealthInfo?>(null) }
    LaunchedEffect(Unit) {
        healthInfo = healthInfoDao.getLatest()
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            HealthHeader(user)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                if (healthInfo != null) {
                    healthInfo!!.run {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "${recordTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "收缩压\n${systolicBloodPressure} mmHg",
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "舒张压\n${diastolicBloodPressure} mmHg",
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "心率\n${heartRate} 次/分钟", textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "风险评估：轻度",
                                color = Color(0xFFFF9800),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("未找到数据")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
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