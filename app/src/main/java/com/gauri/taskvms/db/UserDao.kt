package com.gauri.taskvms.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(userEntity: UserEntity)

    @Query("UPDATE UserEntity SET isSynced = :status WHERE id = :id")
    suspend fun updateStatus(status:Boolean,id:Int)

    @Query("SELECT * FROM UserEntity")
    suspend fun getAllUser():List<UserEntity>
}