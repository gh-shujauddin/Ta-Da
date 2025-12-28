package com.qadri.to_do.ui.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qadri.to_do.data.repository.TaskRepository
import com.qadri.to_do.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val getAllTasks = taskRepository.getAllTask()
        .map { task -> task.sortedBy { it.isCompleted } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            listOf()
        )

    fun updateIsCompleted(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.markItemAsCompleted(task)
    }

    fun deleteAllTasks() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.deleteAllTasks()
    }

    fun deleteCompleted() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.deleteCompletedTasks()
    }

    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.deleteTask(task)
    }

    companion object {
        private val TAG = HomeScreenViewModel::class.simpleName
    }
}
