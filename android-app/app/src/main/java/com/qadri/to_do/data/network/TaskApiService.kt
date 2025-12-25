package com.qadri.to_do.data.network

import com.qadri.to_do.model.dto.TaskDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApiService {

    @GET("api/tasks")
    suspend fun getAllTasks(): List<TaskDto>

    @PUT("api/tasks")
    suspend fun updateTask(@Path("id") id: Long, @Body task: TaskDto): TaskDto

    @DELETE("api/tasks")
    suspend fun deleteTaskById(@Path("id") id: Long)
}