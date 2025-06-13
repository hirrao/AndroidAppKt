package com.hirrao.appktm.display

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hirrao.appktm.data.Config
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstLaunchDialog(
    initialConfig: Config,
    onSave: (Config) -> Unit
) {
    var userName by remember { mutableStateOf(initialConfig.userName) }
    var sex by remember { mutableStateOf(initialConfig.sex) }
    var birthday by remember { mutableStateOf(initialConfig.birthday) }
    var showSexMenu by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val sexOptions = listOf("男", "女")
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = birthday.toEpochDay() * 24 * 60 * 60 * 1000)
    AlertDialog(
        onDismissRequest = {},
        title = { Text("完善个人信息") },
        text = {
            Column {
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("用户名") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    OutlinedTextField(
                        value = sex,
                        onValueChange = {},
                        label = { Text("性别") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        readOnly = true,
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showSexMenu = true }
                    ) {}
                    DropdownMenu(
                        expanded = showSexMenu,
                        onDismissRequest = { showSexMenu = false }
                    ) {
                        sexOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    sex = it
                                    showSexMenu = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    OutlinedTextField(
                        value = birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        onValueChange = {},
                        label = { Text("生日") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        readOnly = true,
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showDatePicker = true }
                    ) {}
                    if (showDatePicker) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    val millis = datePickerState.selectedDateMillis
                                    if (millis != null) {
                                        birthday = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                                    }
                                    showDatePicker = false
                                }) { Text("确定") }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = false }) { Text("取消") }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    initialConfig.copy(
                        userName = userName,
                        sex = sex,
                        birthday = birthday,
                        isFirstLaunch = false
                    )
                )
            }) { Text("保存") }
        }
    )
}