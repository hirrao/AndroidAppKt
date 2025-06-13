package com.hirrao.appktp2.display

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
import androidx.core.net.toUri
import com.hirrao.appktp2.App.Companion.contentResolver
import com.hirrao.appktp2.R
import com.hirrao.appktp2.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview
@Composable
fun DisplayColumn() {
    var items by remember { mutableStateOf(List(0) { User(0, "", 0, 0.0) }) }
    val uri = "content://com.hirrao.appktp.data.Provider".toUri()
    CoroutineScope(Dispatchers.IO).launch {
        items = try {
            contentResolver.query(
                uri, null, // projection
                null, // selection
                null, // selectionArgs
                null // sortOrder
            )?.use { cursor ->
                val idIndex = cursor.getColumnIndexOrThrow("id")
                val nameIndex = cursor.getColumnIndexOrThrow("name")
                val ageIndex = cursor.getColumnIndexOrThrow("age")
                val heightIndex = cursor.getColumnIndexOrThrow("height")
                generateSequence {
                    if (cursor.moveToNext()) {
                        User(
                            cursor.getInt(idIndex),
                            cursor.getString(nameIndex),
                            cursor.getInt(ageIndex),
                            cursor.getDouble(heightIndex)
                        )
                    } else null
                }.toList()
            } ?: emptyList(
            )
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