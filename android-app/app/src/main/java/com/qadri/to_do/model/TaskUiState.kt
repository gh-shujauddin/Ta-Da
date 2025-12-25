package com.qadri.to_do.model

data class TaskUiState(
    val task: Task = Task(1, "", "", false),
    val isEntryValid: Boolean = false
)