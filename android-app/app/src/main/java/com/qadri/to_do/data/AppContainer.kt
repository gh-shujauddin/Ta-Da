package com.qadri.to_do.data

import android.content.Context

interface AppContainer {
    val taskRepository: TaskRepository
}

class AppDataContainer(context: Context): AppContainer {
    override val taskRepository: TaskRepository by lazy {
        OfflineTaskRepository(ToDoDatabase.getDatabase(context).taskDao())
    }
}