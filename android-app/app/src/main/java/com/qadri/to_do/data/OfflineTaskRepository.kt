package com.qadri.to_do.data

import com.qadri.to_do.data.repository.TaskRepository
import com.qadri.to_do.data.room.TaskDao
import com.qadri.to_do.data.room.TaskEntity
import com.qadri.to_do.model.Task
import com.qadri.to_do.model.dto.TaskDto
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

    override suspend fun getTask(id: Long): Task? = taskDao.getTask(id)?.toTask()

    override suspend fun insertTask(task: TaskDto, synced: Boolean) {
        taskDao.insertTask(task.toTaskEntity().copy(isSynced = synced))
    }

    override suspend fun insertLocalTask(task: Task) {
        taskDao.insertTask(task.toTaskEntity())
    }

    override suspend fun updateItem(task: Task) = taskDao.updateItem(task.toTaskEntity())

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task.id)

    override suspend fun deleteTaskPermanently(task: TaskEntity) {
        return taskDao.deleteTaskPermanently(task)
    }

    override suspend fun deleteAllTasks() = taskDao.deleteAllTasks()

    override suspend fun deleteCompletedTasks() = taskDao.deleteCompletedTasks()

    override suspend fun markTaskAsSynced(id: Long, synced: Boolean) =
        taskDao.markTaskAsSynced(listOf(id), synced)

    override suspend fun getAllUnSyncedTasks(): List<TaskEntity> {
        return taskDao.getAllUnSyncedTasks()
    }

    override suspend fun getAllDeletedTasks(): List<TaskEntity> = taskDao.getAllDeletedTasks()

    override suspend fun updateAndMarkAsSynced(tasks: List<TaskDto>) {
        taskDao.updateAndMarkAsSynced(tasks.map { it.toTaskEntity() })
    }
}