package com.qadri.to_do.model

data class TaskUiState(
    val task: Task? = null,
    val isEntryValid: Boolean = false
)