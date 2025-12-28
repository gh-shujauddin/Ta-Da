package com.qadri.to_do.hilt

import android.content.Context
import androidx.work.WorkManager
import com.qadri.to_do.data.OfflineTaskRepository
import com.qadri.to_do.data.RemoteTaskRepositoryImpl
import com.qadri.to_do.data.network.TaskApiService
import com.qadri.to_do.data.prefs.SyncSettingsManager
import com.qadri.to_do.data.repository.RemoteTaskRepository
import com.qadri.to_do.data.repository.TaskRepository
import com.qadri.to_do.data.room.TaskDao
import com.qadri.to_do.data.room.ToDoDatabase
import com.qadri.to_do.data.usecase.BackgroundSyncUseCase
import com.qadri.to_do.workers.SyncScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideTaskRepository(taskDao: TaskDao, syncScheduler: SyncScheduler): TaskRepository {
        return OfflineTaskRepository(taskDao, syncScheduler)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient{
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun providedRetrofit(client: OkHttpClient): Retrofit {
        val baseUrl = "http://10.0.2.2:8080/api/tasks/"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
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

    @Singleton
    @Provides
    fun providesWorker(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}