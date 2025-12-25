package com.qadri.to_do.model.mappers

import com.qadri.to_do.data.room.TaskEntity
import com.qadri.to_do.model.Task

fun Task.toTaskEntity(): TaskEntity = TaskEntity(
    id = id,
    taskName = taskName,
    taskDescription = taskDescription,
    isCompleted = isCompleted,
    isSynced = false,
    isDeleted = false,
    lastUpdateTime = 0,
    createdAt = 0
)