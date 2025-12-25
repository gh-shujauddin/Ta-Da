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
import com.qadri.to_do.model.TaskUiState
import com.qadri.to_do.model.mappers.toTask
import com.qadri.to_do.model.mappers.toTaskUiState
import com.qadri.to_do.ui.homescreen.TaskDetails
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
                taskUiState = taskRepository.getTask(taskId)
                    .filterNotNull()
                    .first()
                    .toTaskUiState(true)
            }
        }
    }

    fun updateUiState(task: TaskDetails) {
        taskUiState = TaskUiState(taskDetails = task, isEntryValid = validateInput(task))
    }

    fun updateTask() = viewModelScope.launch {
        taskRepository.updateItem(taskUiState.taskDetails.toTask())
    }

    fun deleteTask() = viewModelScope.launch {
        taskRepository.deleteTask(taskUiState.taskDetails.toTask())
    }

    private fun validateInput(taskDetails: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(taskDetails) {
            taskName.isNotBlank()
        }
    }

    suspend fun saveTask() {
        if (validateInput()) {
            taskRepository.insertTask(taskUiState.taskDetails.toTask())
        } else {
            Log.d(TAG, "Input not validated")
        }
    }

    companion object {
        private val TAG = TaskEditViewModel::class.simpleName
    }
}