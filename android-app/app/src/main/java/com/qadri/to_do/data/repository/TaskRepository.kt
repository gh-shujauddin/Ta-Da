package com.qadri.to_do.data.repository

import com.qadri.to_do.data.room.TaskEntity
import com.qadri.to_do.model.Task
import com.qadri.to_do.model.dto.TaskDto
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTask(): Flow<List<Task>>

    suspend fun getTask(id: Long): Task?

    suspend fun insertTask(task: TaskDto, synced: Boolean = false)
    suspend fun insertLocalTask(task: Task)

    suspend fun upsertItem(task: Task)
    suspend fun markItemAsCompleted(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun deleteAllTasks()

    suspend fun deleteCompletedTasks()

    suspend fun markTaskAsSynced(id: Long, synced: Boolean)

    suspend fun getAllUnSyncedTasks(): List<TaskEntity>
    suspend fun getAllDeletedTasks(): List<TaskEntity>
    suspend fun updateAndMarkAsSynced(tasks: List<TaskDto>) {}
}