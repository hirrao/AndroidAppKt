package com.hirrao.appktp2.data

import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.IBinder
import androidx.core.net.toUri
import com.hirrao.appktp2.App

class InfoCheckService : Service() {
    private var running = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            while (running) {
                checkAndFixHeight()
                Thread.sleep(5000) // 每5秒检查一次
            }
        }.start()
        return START_STICKY
    }

    private fun checkAndFixHeight() {
        var items = List(0) { User(0, "", 0, 0.0) }
        val uri = "content://com.hirrao.appktp.data.Provider".toUri()
        items = try {
            App.Companion.contentResolver.query(
                uri, null,
                null,
                null,
                null
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
        items.forEach {
            if (it.height < 0.0 || it.height > 300) {
                contentResolver.update(
                    uri, ContentValues().apply {
                        put("name", it.name)
                        put("age", it.age)
                        put("height", 0.0)
                    }, "id=?", arrayOf(it.id.toString())
                )
            }
        }
    }

    override fun onDestroy() {
        running = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}