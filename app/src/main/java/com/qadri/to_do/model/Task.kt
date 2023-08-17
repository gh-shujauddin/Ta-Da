package com.qadri.to_do.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "task_name")
    val taskName: String,
    @ColumnInfo(name = "task_description")
    val taskDescription: String,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean
)
