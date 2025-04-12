package com.hirrao.appktp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: Int): User?

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}