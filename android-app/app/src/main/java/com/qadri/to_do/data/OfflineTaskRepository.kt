package com.qadri.to_do.data

import com.qadri.to_do.model.Task
import kotlinx.coroutines.flow.Flow

class OfflineTaskRepository(private val taskDao: TaskDao): TaskRepository {
    override fun getAllTask(): Flow<List<Task>> = taskDao.getAllTask()

    override fun getTask(id: Long): Flow<Task> = taskDao.getTask(id)

    override suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    override suspend fun updateItem(task: Task) = taskDao.updateItem(task)

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    override suspend fun deleteAllTasks() = taskDao.deleteAllTasks()

    override suspend fun deleteCompletedTasks() = taskDao.deleteCompletedTasks()
}