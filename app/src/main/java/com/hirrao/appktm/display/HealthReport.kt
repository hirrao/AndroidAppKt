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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hirrao.appktm.App
import com.hirrao.appktm.data.Config

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
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
fun HealthHeader(user: Config) {
    Text("${user.userName}    ${user.sex} | ${user.birthday}", fontSize = 16.sp)
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun HealthAppScreen() {
    var currentPage by remember { mutableIntStateOf(0) }
    val configDao = App.configDao
    var config by remember { mutableStateOf<Config>(Config()) }
    LaunchedEffect(Unit) {
        config = configDao.get()!!
    }
    val user = config
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
            0 -> HealthOverviewPage(user)
            1 -> HealthDetailPage()
            2 -> MedicineManagePage(user)
        }

    }
}