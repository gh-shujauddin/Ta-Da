package com.qadri.to_do.data.repository

import com.qadri.to_do.data.room.TaskEntity
import com.qadri.to_do.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTask(): Flow<List<Task>>

    fun getTask(id: Long): Flow<Task>

    suspend fun insertTask(task: Task)

    suspend fun updateItem(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun deleteTaskPermanently(task: TaskEntity)

    suspend fun deleteAllTasks()

    suspend fun deleteCompletedTasks()

    suspend fun markTaskAsSynced(id: Long)

    suspend fun getAllUnSyncedTasks(): List<TaskEntity>
    suspend fun getAllDeletedTasks(): List<TaskEntity>
}