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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hirrao.appktp.data.User
import com.hirrao.appktp.theme.AppTheme

@Composable
fun CommonInputField(
    modifier: Modifier = Modifier
) {
    var user by remember { mutableStateOf(User("", "", 0, 0.0)) }
    var ageText by remember { mutableStateOf("") }
    var heightText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    user.run {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val textModifier = Modifier
                .padding(8.dp)
                .height(64.dp)
                .fillMaxWidth()
            TextField(
                modifier = textModifier,
                value = id,
                onValueChange = { id = it },
                label = { Text("ID") })
            TextField(
                modifier = textModifier,
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") })
            TextField(modifier = textModifier, value = ageText, onValueChange = {
                ageText = it
                age = ageText.toIntOrNull() ?: 0
            }, label = { Text("Age") })
            TextField(modifier = textModifier, value = heightText, onValueChange = {
                heightText = it
                height = heightText.toDoubleOrNull() ?: 0.0
            }, label = { Text("Height") })
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
}


@Preview(showBackground = true, widthDp = 360)
@Composable
fun CommonInputFieldPreview() {
    AppTheme {
        CommonInputField()
    }
}