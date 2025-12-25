package com.qadri.to_do.data

import com.qadri.to_do.data.network.TaskApiService
import com.qadri.to_do.data.repository.RemoteTaskRepository
import com.qadri.to_do.model.dto.TaskDto

class RemoteTaskRepositoryImpl(
    private val apiService: TaskApiService
) : RemoteTaskRepository {
    override suspend fun getAllTasks(): Result<List<TaskDto>> {
        return try {
            val tasks = apiService.getAllTasks()
            Result.success(tasks)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun updateTask(task: TaskDto): Result<TaskDto> {
        return try {
            val task = apiService.updateTask(task.id, task)
            Result.success(task)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun deleteTask(task: TaskDto): Result<Unit> {
        return try {
            val task = apiService.d(task.id, task)
            Result.success(task)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}