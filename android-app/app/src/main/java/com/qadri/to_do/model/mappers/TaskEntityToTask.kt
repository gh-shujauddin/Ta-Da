package com.qadri.to_do.model.mappers

import com.qadri.to_do.data.room.TaskEntity
import com.qadri.to_do.model.Task

fun TaskEntity.toTask(): Task = Task(
    id = id,
    taskName = taskName,
    taskDescription = taskDescription,
    isCompleted = isCompleted
)
