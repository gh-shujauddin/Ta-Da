package com.qadri.to_do.hilt

import android.content.Context
import com.qadri.to_do.data.OfflineTaskRepository
import com.qadri.to_do.data.TaskDao
import com.qadri.to_do.data.TaskRepository
import com.qadri.to_do.data.ToDoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideTaskDao(@ApplicationContext applicationContext: Context): TaskDao {
        return ToDoDatabase.getDatabase(applicationContext).taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return OfflineTaskRepository(taskDao)
    }

}