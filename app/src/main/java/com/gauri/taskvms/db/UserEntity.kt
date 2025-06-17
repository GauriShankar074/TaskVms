package com.gauri.taskvms.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.processing.Generated

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val dob: String,
    val isWorking: Boolean,
    val currentCompany: String,
    val isSynced:Boolean = false
)