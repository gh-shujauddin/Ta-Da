package com.qadri.to_do.model.mappers

import com.qadri.to_do.data.room.TaskEntity
import com.qadri.to_do.model.dto.TaskDto

fun TaskEntity.toTaskDto() = TaskDto(
    id = id,
    taskName = taskName,
    taskDescription = taskDescription,
    isCompleted = isCompleted,
    lastUpdateTime = lastUpdateTime,
    createdAt = createdAt
)

fun TaskDto.toTaskEntity() = TaskEntity(
    id = id,
    taskName = taskName,
    taskDescription = taskDescription,
    isCompleted = isCompleted,
    lastUpdateTime = lastUpdateTime,
    createdAt = createdAt
)