package com.qadri.to_do.data.network

import com.qadri.to_do.model.Task
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApiService {

    @GET("api/tasks")
    suspend fun getAllTasks(): List<Task>

    @POST("api/tasks")
    suspend fun createTask(@Body task: Task): Task

    @PUT("api/tasks")
    suspend fun updateTask(@Path("id") id: Long, @Body task: Task): Task

}