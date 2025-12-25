package com.qadri.to_do.ui.homescreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.qadri.to_do.TaskApplication
import com.qadri.to_do.data.repository.TaskRepository
import com.qadri.to_do.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    init {
        syncTasks()
    }

    val getAllTasks = taskRepository.getAllTask()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            listOf()
        )

    fun updateIsCompleted(task: Task) = viewModelScope.launch {
        taskRepository.updateItem(task.copy(isCompleted = !task.isCompleted))
    }

    fun deleteAllTasks() = viewModelScope.launch {
        taskRepository.deleteAllTasks()
    }

    fun deleteCompleted() = viewModelScope.launch {
        taskRepository.deleteCompletedTasks()
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskRepository.deleteTask(task)
    }

    private fun syncTasks() {
        viewModelScope.launch {
            val tasks = taskRepository.getAllUnSyncedTasks()
                .collect {
                    Log.d(TAG, it.toString())
                }
        }
    }

    companion object {
        private val TAG = HomeScreenViewModel::class.simpleName
    }
}
