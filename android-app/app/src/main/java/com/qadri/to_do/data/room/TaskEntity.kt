package com.qadri.to_do.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "task_name")
    val taskName: String,
    @ColumnInfo(name = "task_description")
    val taskDescription: String,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false,
    val lastUpdateTime: Long,
    val createdAt: Long
)
