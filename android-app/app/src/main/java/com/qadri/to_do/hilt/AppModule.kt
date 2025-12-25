package com.qadri.to_do.hilt

import android.content.Context
import com.qadri.to_do.data.OfflineTaskRepository
import com.qadri.to_do.data.RemoteTaskRepositoryImpl
import com.qadri.to_do.data.network.RetrofitInstance
import com.qadri.to_do.data.network.TaskApiService
import com.qadri.to_do.data.prefs.SyncSettingsManager
import com.qadri.to_do.data.repository.RemoteTaskRepository
import com.qadri.to_do.data.room.TaskDao
import com.qadri.to_do.data.repository.TaskRepository
import com.qadri.to_do.data.room.ToDoDatabase
import com.qadri.to_do.data.usecase.BackgroundSyncUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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

    @Provides
    @Singleton
    fun providedRetrofit(): Retrofit {
        return RetrofitInstance.instance
    }

    @Provides
    @Singleton
    fun providesTaskApiService(retrofit: Retrofit): TaskApiService {
        return retrofit.create(TaskApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(taskApiService: TaskApiService): RemoteTaskRepository {
        return RemoteTaskRepositoryImpl(taskApiService)
    }

    @Provides
    @Singleton
    fun providedBgSyncUseCase(
        taskDao: TaskDao,
        onlineRepository: RemoteTaskRepository,
        syncSettingsManager: SyncSettingsManager
    ): BackgroundSyncUseCase {
        return BackgroundSyncUseCase(taskDao, onlineRepository, syncSettingsManager)
    }

}