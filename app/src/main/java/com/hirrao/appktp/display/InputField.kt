package com.hirrao.appktp.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.hirrao.appktp.theme.AppTheme

@Composable
fun CommonInputField(
    modifier: Modifier = Modifier
) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableIntStateOf(0) }
    var height by remember { mutableDoubleStateOf(0.0) }
    var ageText by remember { mutableStateOf("") }
    var heightText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var ageError by remember { mutableStateOf(false) }
    var heightError by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val textModifier = Modifier
            .padding(8.dp)
            .height(72.dp)
            .fillMaxWidth()
        TextField(modifier = textModifier, value = id, onValueChange = {
            id = it
        }, label = { Text("ID") })
        TextField(modifier = textModifier, value = name, onValueChange = {
            name = it
        }, label = { Text("Name") })
        TextField(modifier = textModifier, value = ageText, isError = ageError, supportingText = {
            if (ageError) {
                Text("请输入正确的年龄")
            }
        }, onValueChange = {
            ageText = it
            ageError = !ageText.isDigitsOnly()
            age = ageText.toIntOrNull() ?: 0
        }, label = { Text("Age") })
        TextField(
            modifier = textModifier,
            value = heightText,
            isError = heightError,
            supportingText = {
                if (heightError) {
                    Text("请输入正确的身高")
                }
            },
            onValueChange = {
                heightText = it
                heightError = !heightText.isDigitsOnly()
                height = heightText.toDoubleOrNull() ?: 0.0
            },
            label = { Text("Height") })
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            val buttonModifier = Modifier
                .padding(16.dp)
                .width(128.dp)
                .height(54.dp)
            Button(modifier = buttonModifier, onClick = { }) {
                Text("信息处理")
            }
            Button(modifier = buttonModifier, onClick = { }) {
                Text("信息列出")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun CommonInputFieldPreview() {
    AppTheme {
        CommonInputField()
    }
}