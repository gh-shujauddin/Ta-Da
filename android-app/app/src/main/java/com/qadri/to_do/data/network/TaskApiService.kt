package com.qadri.to_do.data.network

import com.qadri.to_do.model.dto.TaskDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApiService {

    @GET("all")
    suspend fun getAllTasks(@Query("lastSyncTime") lastSyncTime: Long?): Response<List<TaskDto>>

    @PUT("{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body task: TaskDto): Response<TaskDto>

    @DELETE("{id}")
    suspend fun deleteTaskById(@Path("id") id: Long): Response<Unit>
}