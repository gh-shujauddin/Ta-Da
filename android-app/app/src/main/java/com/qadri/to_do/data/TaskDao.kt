package com.qadri.to_do.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.qadri.to_do.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("select * from task order by id DESC, is_completed ASC")
    fun getAllTask(): Flow<List<Task>>

    @Query("select * from task where id = :id")
    fun getTask(id: Long): Flow<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateItem(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("delete from task")
    suspend fun deleteAllTasks()

    @Query("delete from task where is_completed = 1")
    suspend fun deleteCompletedTasks()
}