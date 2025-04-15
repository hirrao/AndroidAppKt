package com.hirrao.appktp.display

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hirrao.appktp.App.Companion.userDao
import com.hirrao.appktp.R
import com.hirrao.appktp.data.User
import com.hirrao.appktp.enums.DialogDisplayEnums
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDialog(
    user: User, showDialog: MutableState<Boolean>, dialogState: MutableState<DialogDisplayEnums>
) {
    val text = "${stringResource(R.string.process_dialog_description_1)}\n" + "${
        stringResource(
            R.string.id_name
        )
    } : ${user.id}\n" + "${
        stringResource(
            R.string.name_name
        )
    } : ${user.name}\n" + "${
        stringResource(
            R.string.age_name
        )
    } : ${user.age}\n" + "${
        stringResource(
            R.string.height_name
        )
    } : ${user.height}"
    CommonDialog(modifier = Modifier, onDismissRequest = {
        showDialog.value = false
    }, text = text) {
        val buttonModifier = Modifier.padding(4.dp)
        TextButton(
            onClick = {
                showDialog.value = false
                CoroutineScope(Dispatchers.IO).launch {
                    val selectUser = userDao.getById(user.id)
                    if (selectUser == null) {
                        userDao.insert(user)
                        dialogState.value = DialogDisplayEnums.INSERT_SUCCESS
                    } else dialogState.value = DialogDisplayEnums.INSERT_ERROR
                }
            }, modifier = buttonModifier
        ) {
            Text(stringResource(R.string.process_dialog_button_1))
        }
        TextButton(
            onClick = { showDialog.value = false }, modifier = buttonModifier
        ) {
            Text(stringResource(R.string.process_dialog_button_2))
        }
        TextButton(
            onClick = { showDialog.value = false }, modifier = buttonModifier
        ) {
            Text(stringResource(R.string.process_dialog_button_3))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultDialog(dialogState: MutableState<DialogDisplayEnums>) {
    CommonDialog(
        onDismissRequest = { dialogState.value = DialogDisplayEnums.NONE },
        modifier = Modifier,
        text = "Empty"
    ) {
        val buttonModifier = Modifier.padding(4.dp)
        TextButton(
            modifier = buttonModifier, onClick = { dialogState.value = DialogDisplayEnums.NONE }) {
            Text("Dismiss")
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CommonDialog(
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    text: String,
    buttons: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val dialogHeight = screenHeight * 0.3f
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth()
            .height(dialogHeight)
            .then(modifier)
    ) {
        Surface(
            modifier = Modifier,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text)
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.padding(2.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    buttons()
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Preview(
    name = "Main-Preview-zhCN-dark",
    group = "UserDialog",
    widthDp = 360,
    locale = "zh-rCN",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Main-Preview-enUS-light",
    group = "UserDialog",
    showBackground = true,
    widthDp = 360,
    locale = "en-rUS",
    uiMode = UI_MODE_NIGHT_NO or UI_MODE_TYPE_NORMAL
)
@Composable
fun UserDialogPreview() {
    UserDialog(User(0, "test", 0, 0.0), remember { mutableStateOf(true) }, remember {
        mutableStateOf(
            DialogDisplayEnums.NONE
        )
    })
}

@Preview(group = "ResultDialog")
@Composable
fun ResultDialogInsertSuccessPreview() {
    ResultDialog(remember { mutableStateOf(DialogDisplayEnums.INSERT_SUCCESS) })
}

@Preview(group = "ResultDialog")
@Composable
fun ResultDialogInsertErrorPreview() {
    ResultDialog(remember { mutableStateOf(DialogDisplayEnums.INSERT_ERROR) })
}

@Preview(group = "ResultDialog")
@Composable
fun ResultDialogUpdateSuccessPreview() {
    ResultDialog(remember { mutableStateOf(DialogDisplayEnums.UPDATE_SUCCESS) })
}

@Preview(group = "ResultDialog")
@Composable
fun ResultDialogDeleteSuccessPreview() {
    ResultDialog(remember { mutableStateOf(DialogDisplayEnums.DELETE_SUCCESS) })
}

@Preview(group = "ResultDialog")
@Composable
fun ResultDialogDeleteErrorPreview() {
    ResultDialog(remember { mutableStateOf(DialogDisplayEnums.DELETE_ERROR) })
}