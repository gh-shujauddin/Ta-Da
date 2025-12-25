package com.qadri.to_do.ui.add_edit_task

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.qadri.to_do.data.repository.TaskRepository
import com.qadri.to_do.model.Task
import com.qadri.to_do.model.TaskUiState
import com.qadri.to_do.model.mappers.toTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    private val taskId: Long? = savedStateHandle.toRoute<TaskEditDestination>().taskId

    init {
        taskId?.let {
            viewModelScope.launch {
                taskUiState = (taskRepository.getTask(taskId) ?: Task()).toTaskUiState(true)
            }
        }
    }

    fun updateUiState(task: Task) {
        taskUiState = TaskUiState(task = task, isEntryValid = validateInput(task))
    }

    fun updateTask() = viewModelScope.launch {
        val task = taskUiState.task
        taskRepository.updateItem(task)
    }

    fun deleteTask() = viewModelScope.launch {
        val task = taskUiState.task
        taskRepository.deleteTask(task)
    }

    private fun validateInput(task: Task? = taskUiState.task): Boolean {
        return with(task) {
            this?.taskName?.isNotBlank() ?: false
        }
    }

    suspend fun saveTask() {
        if (validateInput()) {
            taskRepository.insertLocalTask(taskUiState.task)
        } else {
            Log.d(TAG, "Input not validated")
        }
    }

    companion object {
        private val TAG = TaskEditViewModel::class.simpleName
    }
}