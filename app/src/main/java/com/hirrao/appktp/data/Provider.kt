package com.hirrao.appktp.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.hirrao.appktp.App

class Provider : ContentProvider() {
    private val userDao by lazy { App.userDao }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = MatrixCursor(arrayOf("id", "name", "age", "height"))
        if (selection == "id=?" && selectionArgs != null && selectionArgs.isNotEmpty()) {
            val id = selectionArgs[0].toIntOrNull()
            val user = id?.let { userDao.getById(it) }
            user?.let {
                cursor.addRow(arrayOf(it.id, it.name, it.age, it.height))
            }
        } else {
            val users = userDao.getAll()
            users.forEach {
                cursor.addRow(arrayOf(it.id, it.name, it.age, it.height))
            }
        }
        return cursor

    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val user = User(
            id = values?.getAsInteger("id") ?: 0,
            name = values?.getAsString("name") ?: "",
            age = values?.getAsInteger("age") ?: 0,
            height = values?.getAsDouble("height") ?: 0.0,
        )
        userDao.insert(user)
        return uri
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?
    ): Int {
        val user = User(
            id = values?.getAsInteger("id") ?: 0,
            name = values?.getAsString("name") ?: "",
            age = values?.getAsInteger("age") ?: 0,
            height = values?.getAsDouble("height") ?: 0.0,
        )
        userDao.update(user)
        return 1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        if (selectionArgs != null && selectionArgs.isNotEmpty()) {
            val id = selectionArgs[0].toIntOrNull() ?: return 0
            val user = userDao.getById(id) ?: return 0
            userDao.delete(user)
            return 1
        }
        return 0
    }

    override fun getType(p0: Uri): String? = "vnd.android.cursor.dir/vnd.hirrao"
}