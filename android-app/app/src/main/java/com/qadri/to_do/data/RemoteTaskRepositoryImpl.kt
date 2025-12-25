package com.qadri.to_do.data

import com.qadri.to_do.data.network.TaskApiService
import com.qadri.to_do.data.repository.RemoteTaskRepository
import com.qadri.to_do.model.dto.TaskDto

class RemoteTaskRepositoryImpl(
    private val apiService: TaskApiService
) : RemoteTaskRepository {
    override suspend fun getAllTasks(lastSyncTime: Long?): Result<List<TaskDto>> {
        return try {
            val response = apiService.getAllTasks(lastSyncTime)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Server error: ${response.code()} ${response.errorBody()}"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun updateTask(task: TaskDto): Result<TaskDto> {
        return try {
            val response = apiService.updateTask(task.id, task)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Server error: ${response.code()} ${response.errorBody()}"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun deleteTask(task: TaskDto): Result<Unit> {
        return try {
            val response = apiService.deleteTaskById(task.id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Server error: ${response.code()} ${response.errorBody()}"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}