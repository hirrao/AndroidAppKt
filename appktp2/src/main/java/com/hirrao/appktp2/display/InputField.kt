package com.hirrao.appktp2.display

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.hirrao.appktp2.R
import com.hirrao.appktp2.data.User
import com.hirrao.appktp2.enums.DialogDisplayEnums
import com.hirrao.appktp2.theme.Red40
import com.hirrao.appktp2.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Main-Preview-zhCN-dark",
    widthDp = 360,
    locale = "zh-rCN",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Main-Preview-enUS-light",
    showBackground = true,
    widthDp = 360,
    locale = "en-rUS",
    uiMode = UI_MODE_NIGHT_NO or UI_MODE_TYPE_NORMAL
)
@Composable
fun CommonInputField(
    modifier: Modifier = Modifier
) {
    var id = remember { mutableIntStateOf(0) }
    var name = remember { mutableStateOf("") }
    var age = remember { mutableIntStateOf(0) }
    var height = remember { mutableDoubleStateOf(0.0) }
    var showDialog = remember { mutableStateOf(false) }
    var showErrorText = remember { mutableStateOf(false) }
    var dialogState = remember { mutableStateOf(DialogDisplayEnums.NONE) }
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserTextInput(id, name, age, height)
        DataButtons(
            id.intValue,
            name.value,
            age.intValue,
            height.doubleValue,
            showDialog,
            showErrorText,
            dialogState
        )
        if (showErrorText.value) {
            ErrorText()
        }
    }
    if (showDialog.value) {
        UserDialog(
            User(id.intValue, name.value, age.intValue, height.doubleValue), showDialog, dialogState
        )
    }
    if (dialogState.value == DialogDisplayEnums.DISPLAY) {
        BasicAlertDialog(
            onDismissRequest = { dialogState.value = DialogDisplayEnums.NONE },
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
        ) {
            DisplayColumn()
        }
    } else if (dialogState.value != DialogDisplayEnums.NONE) {
        ResultDialog(dialogState)
    }
}

@Composable
fun UserTextInput(
    id: MutableIntState,
    name: MutableState<String>,
    age: MutableIntState,
    height: MutableDoubleState
) {
    var idText by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("") }
    var heightText by remember { mutableStateOf("") }
    var idError by remember { mutableStateOf(false) }
    var ageError by remember { mutableStateOf(false) }
    var heightError by remember { mutableStateOf(false) }
    val textModifier = Modifier
        .padding(8.dp)
        .height(72.dp)
        .fillMaxWidth()
    TextField(modifier = textModifier, value = idText, isError = idError, supportingText = {
        if (idError) {
            Text(stringResource(R.string.input_error, stringResource(R.string.id_name)))
        }
    }, onValueChange = {
        idText = it
        idError = !idText.isDigitsOnly()
        id.intValue = idText.toIntOrNull() ?: 0
    }, label = { Text(stringResource(R.string.id_name)) })
    TextField(modifier = textModifier, value = name.value, onValueChange = {
        name.value = it
    }, label = { Text(stringResource(R.string.name_name)) })
    TextField(modifier = textModifier, value = ageText, isError = ageError, supportingText = {
        if (ageError) {
            Text(stringResource(R.string.input_error, stringResource(R.string.age_name)))
        }
    }, onValueChange = {
        ageText = it
        ageError = !ageText.isDigitsOnly()
        age.intValue = ageText.toIntOrNull() ?: 0
    }, label = { Text(stringResource(R.string.age_name)) })
    TextField(modifier = textModifier, value = heightText, isError = heightError, supportingText = {
        if (heightError) {
            Text(stringResource(R.string.input_error, stringResource(R.string.height_name)))
        }
    }, onValueChange = {
        heightText = it
        heightError = !heightText.isDigitsOnly()
        height.doubleValue = heightText.toDoubleOrNull() ?: 0.0
    }, label = { Text(stringResource(R.string.height_name)) })
}

@Composable
fun DataButtons(
    id: Int,
    name: String,
    age: Int,
    height: Double,
    showDialog: MutableState<Boolean>,
    showErrorText: MutableState<Boolean>,
    dialogState: MutableState<DialogDisplayEnums>
) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        val buttonModifier = Modifier
            .padding(16.dp)
            .width(128.dp)
            .height(54.dp)
        Button(modifier = buttonModifier, onClick = {
            if (id == 0 || name.isEmpty() || age == 0 || height == 0.0) {
                showErrorText.value = true
            } else {
                showErrorText.value = false
                showDialog.value = true
            }
        }) {
            Text(stringResource(R.string.data_button_1))
        }
        Button(modifier = buttonModifier, onClick = {
            dialogState.value = DialogDisplayEnums.DISPLAY
        }) {
            Text(stringResource(R.string.data_button_2))
        }
    }
}

@Preview
@Composable
fun ErrorText() {
    val modifier =
        Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    val style = TextStyle(
        color = Red40, fontSize = 16.sp
    )
    Text(modifier = modifier, text = stringResource(R.string.input_error_text), style = style)
}
