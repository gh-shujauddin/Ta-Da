package com.qadri.to_do.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("select * from task where isDeleted = 0 order by id DESC, is_completed ASC")
    fun getAllTask(): Flow<List<TaskEntity>>

    @Query("select * from task where id = :id")
    fun getTask(id: Long): Flow<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateItem(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("delete from task")
    suspend fun deleteAllTasks()

    @Query("delete from task where is_completed = 1")
    suspend fun deleteCompletedTasks()

    @Query("select * from task where isSynced = 0")
    fun getAllUnSyncedTasks(): Flow<List<TaskEntity>>
}