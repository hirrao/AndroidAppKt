package com.hirrao.appktp.display

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hirrao.appktp.App.Companion.userDao
import com.hirrao.appktp.R
import com.hirrao.appktp.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview
@Composable
fun DisplayColumn() {
    var items by remember { mutableStateOf(List(0) { User(0, "", 0, 0.0) }) }
    CoroutineScope(Dispatchers.IO).launch {
        items = try {
            userDao.getAll()
        } catch (_: Exception) {
            emptyList()
        }
    }
    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            item {
                Row {
                    Text(stringResource(R.string.id_name), modifier = Modifier.weight(1f))
                    Text(stringResource(R.string.name_name), modifier = Modifier.weight(1f))
                    Text(stringResource(R.string.age_name), modifier = Modifier.weight(1f))
                    Text(stringResource(R.string.height_name), modifier = Modifier.weight(1f))
                }
            }
            items(items) { item ->
                Row {
                    Text(item.id.toString(), modifier = Modifier.weight(1f))
                    Text(item.name, modifier = Modifier.weight(1f))
                    Text(item.age.toString(), modifier = Modifier.weight(1f))
                    Text(item.height.toString(), modifier = Modifier.weight(1f))
                }
            }
        }
    }
}