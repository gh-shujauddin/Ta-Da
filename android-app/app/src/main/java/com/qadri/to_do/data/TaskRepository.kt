package com.qadri.to_do.data

import com.qadri.to_do.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTask(): Flow<List<Task>>

    fun getTask(id: Int): Flow<Task>

    suspend fun insertTask(task: Task)

    suspend fun updateItem(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun deleteAllTasks()

    suspend fun deleteCompletedTasks()
}