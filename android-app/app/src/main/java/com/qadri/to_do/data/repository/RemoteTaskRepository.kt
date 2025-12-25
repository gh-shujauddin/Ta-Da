package com.qadri.to_do.data.repository

import com.qadri.to_do.model.Task
import com.qadri.to_do.model.dto.TaskDto

interface RemoteTaskRepository {
    suspend fun getAllTasks(): Result<List<TaskDto>>
    suspend fun updateTask(task: TaskDto): Result<TaskDto>
    suspend fun deleteTask(task: TaskDto): Result<Unit>
}