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
import com.qadri.to_do.data.TaskRepository
import com.qadri.to_do.model.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    fun updateUiState(task: TaskDetails) {
        taskUiState = TaskUiState(taskdetails = task, isEntryValid = validateInput(task))
    }

    private fun validateInput(taskDetails: TaskDetails = taskUiState.taskdetails): Boolean {
        return with(taskDetails) {
            taskName.isNotBlank()
        }
    }

    suspend fun saveTask() {
        if (validateInput()) {
            taskRepository.insertTask(taskUiState.taskdetails.toTask())
        } else {
            Log.d(TAG, "Input not validatd")
        }
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

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TaskApplication)
                HomeScreenViewModel(
                    application.container.taskRepository
                )
            }
        }

        private val TAG = HomeScreenViewModel::class.simpleName
    }
}

data class TaskUiState(
    val taskdetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val id: Int = 0,
    val taskName: String = "",
    val taskDescription: String = "",
    val isCompleted: Boolean = false
)

fun TaskDetails.toTask(): Task = Task(
    id = id,
    taskName = taskName,
    taskDescription = taskDescription,
    isCompleted = isCompleted
)
