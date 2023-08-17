package com.qadri.to_do.ui.homescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.qadri.to_do.TaskApplication
import com.qadri.to_do.data.TaskRepository
import com.qadri.to_do.model.Task
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskEditViewModel(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    private val taskId: Int = checkNotNull(savedStateHandle[TaskEditDestination.taskIdArg])

    init {
        viewModelScope.launch {
            taskUiState = taskRepository.getTask(taskId)
                .filterNotNull()
                .first()
                .toTaskUiState(true)
        }
    }

    fun updateUiState(task: TaskDetails) {
        taskUiState = TaskUiState(taskdetails = task, isEntryValid = validateInput(task))
    }

    fun updateTask() = viewModelScope.launch {
        taskRepository.updateItem(taskUiState.taskdetails.toTask())
    }

    fun deleteTask() = viewModelScope.launch {
        taskRepository.deleteTask(taskUiState.taskdetails.toTask())
    }

    private fun validateInput(taskDetails: TaskDetails = taskUiState.taskdetails): Boolean {
        return with(taskDetails) {
            taskName.isNotBlank()
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TaskApplication)
                TaskEditViewModel(
                    application.container.taskRepository,
                    this.createSavedStateHandle()
                )
            }
        }
    }
}

fun Task.toTaskUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState(
    taskdetails = this.toTaskDetails(),
    isEntryValid = isEntryValid
)

fun Task.toTaskDetails(): TaskDetails = TaskDetails(
    id = id,
    taskName = taskName,
    taskDescription = taskDescription,
    isCompleted = isCompleted
)