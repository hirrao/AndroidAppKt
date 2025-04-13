package com.hirrao.appktp.data

import com.hirrao.appktp.App.Companion.userDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun insertUser(user: User) {
    CoroutineScope(Dispatchers.IO).launch {
        userDao.insert(user)
    }
}