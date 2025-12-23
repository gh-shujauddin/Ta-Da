package com.qadri.to_do.model.mappers

import com.qadri.to_do.model.Task
import com.qadri.to_do.model.TaskUiState

fun Task.toTaskUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState(
    taskdetails = this.toTaskDetails(),
    isEntryValid = isEntryValid
)
