package com.qadri.to_do.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("select * from task where isDeleted = 0 order by id DESC, is_completed ASC")
    fun getAllTask(): Flow<List<TaskEntity>>

    @Query("select * from task where id = :id")
    suspend fun getTask(id: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateItem(task: TaskEntity)

    @Query("update task set isDeleted = 1 where id=:id")
    suspend fun deleteTask(id: Long)

    @Delete
    suspend fun deleteTaskPermanently(task: TaskEntity)

    @Query("delete from task")
    suspend fun deleteAllTasks()

    @Query("delete from task where is_completed = 1")
    suspend fun deleteCompletedTasks()

    @Query("select * from task where isSynced = 0")
    suspend fun getAllUnSyncedTasks(): List<TaskEntity>

    @Query("update task set isSynced = :synced where id IN (:id)")
    suspend fun markTaskAsSynced(id: List<Long>, synced: Boolean)

    @Query("select * from task where isDeleted = 1")
    suspend fun getAllDeletedTasks(): List<TaskEntity>

    @Update
    suspend fun updateMultipleTasks(tasks: List<TaskEntity>)

    @Transaction
    suspend fun updateAndMarkAsSynced(tasks: List<TaskEntity>) {
        updateMultipleTasks(tasks)

        val ids = tasks.map { it.id }
        markTaskAsSynced(ids, true)
    }
}