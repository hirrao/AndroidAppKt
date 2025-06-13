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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hirrao.appktm.App.Companion.machineInfoDao
import com.hirrao.appktm.data.Config
import com.hirrao.appktm.data.MachineInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun MedicineManagePage(user: Config) {
    var machineList by remember {
        mutableStateOf(listOf<MachineInfo>())
    }
    var editingId by remember { mutableStateOf<Int?>(null) }
    var editName by remember { mutableStateOf("") }
    var editTime by remember { mutableStateOf("") }
    var editDosage by remember { mutableStateOf("") }
    var isEdited by remember { mutableStateOf(false) }
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    LaunchedEffect(Unit) {
        machineList = machineInfoDao.getAll()
    }
    LaunchedEffect(isEdited) {
        if (isEdited) {
            machineList = machineInfoDao.getAll()
            isEdited = false
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFB3E5FC))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                HealthHeader(user)
            }
            items(machineList) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (editingId == item.id) {
                            TextField(
                                value = editName,
                                onValueChange = { editName = it },
                                label = { Text("药名") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = editTime,
                                onValueChange = { editTime = it },
                                label = { Text("时间(HH:mm)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = editDosage,
                                onValueChange = { editDosage = it },
                                label = { Text("剂量(毫克)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Button(onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        machineInfoDao.update(
                                            MachineInfo(
                                                id = item.id,
                                                machineName = editName,
                                                eatTime = LocalTime.parse(editTime, timeFormatter),
                                                dosage = editDosage.toDoubleOrNull() ?: 0.0
                                            )
                                        )
                                    }
                                    editingId = null
                                    isEdited = true
                                }) {
                                    Text("保存")
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        machineInfoDao.delete(item)
                                    }
                                    editingId = null
                                    isEdited = true
                                }) {
                                    Text("删除")
                                }
                            }
                        } else {
                            Text(
                                "${item.machineName} ${item.eatTime.format(timeFormatter)} ${item.dosage}毫克",
                                modifier = Modifier
                                    .clickable {
                                        editingId = item.id
                                        editName = item.machineName
                                        editTime = item.eatTime.format(timeFormatter)
                                        editDosage = item.dosage.toString()
                                    }
                                    .padding(vertical = 16.dp),
                                color = Color.Black)
                        }
                    }
                }
            }
        }
        var showAddDialog by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            Button(onClick = { showAddDialog = true }, modifier = Modifier.padding(24.dp)) {
                Text("添加药品")
            }
        }
        if (showAddDialog) {
            var addName by remember { mutableStateOf("") }
            var addTime by remember { mutableStateOf("") }
            var addDosage by remember { mutableStateOf("") }
            val addTimeValid = addTime.matches(Regex("\\d{2}:\\d{2}"))
            val addDosageValid = addDosage.toDoubleOrNull() != null
            val addNameValid = addName.isNotBlank()
            val addAllValid = addNameValid && addTimeValid && addDosageValid
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("添加药品") },
                text = {
                    Column {
                        TextField(
                            value = addName,
                            onValueChange = { addName = it },
                            label = { Text("药名") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = addTime,
                            onValueChange = { addTime = it },
                            label = { Text("时间(HH:mm)") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = addTime.isNotBlank() && !addTimeValid
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = addDosage,
                            onValueChange = { addDosage = it },
                            label = { Text("剂量(毫克)") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = addDosage.isNotBlank() && !addDosageValid
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                machineInfoDao.insert(
                                    MachineInfo(
                                        machineName = addName, eatTime = try {
                                            LocalTime.parse(addTime, timeFormatter)
                                        } catch (_: Exception) {
                                            LocalTime.of(0, 0)
                                        }, dosage = addDosage.toDoubleOrNull() ?: 0.0
                                    )
                                )
                            }
                            showAddDialog = false
                            isEdited = true
                        }, enabled = addAllValid
                    ) { Text("保存") }
                },
                dismissButton = {
                    Button(onClick = { showAddDialog = false }) { Text("取消") }
                })
        }
    }
}
