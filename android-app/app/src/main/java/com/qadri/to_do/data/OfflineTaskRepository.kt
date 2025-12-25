package com.qadri.to_do.data

import com.qadri.to_do.data.repository.TaskRepository
import com.qadri.to_do.data.room.TaskDao
import com.qadri.to_do.model.Task
import com.qadri.to_do.model.mappers.toTask
import com.qadri.to_do.model.mappers.toTaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineTaskRepository(private val taskDao: TaskDao) : TaskRepository {

    override fun getAllTask(): Flow<List<Task>> =
        taskDao.getAllTask().map {
            it.map { taskEntity ->
                taskEntity.toTask()
            }
        }

    override fun getTask(id: Long): Flow<Task> = taskDao.getTask(id).map { it.toTask() }

    override suspend fun insertTask(task: Task) = taskDao.insertTask(task.toTaskEntity())

    override suspend fun updateItem(task: Task) = taskDao.updateItem(task.toTaskEntity())

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task.toTaskEntity())

    override suspend fun deleteAllTasks() = taskDao.deleteAllTasks()

    override suspend fun deleteCompletedTasks() = taskDao.deleteCompletedTasks()

    override suspend fun getAllUnSyncedTasks(): Flow<List<Task>> {
        return taskDao.getAllUnSyncedTasks()
            .map {
                it.map { taskEntity ->
                    taskEntity.toTask()
                }
            }
    }
}