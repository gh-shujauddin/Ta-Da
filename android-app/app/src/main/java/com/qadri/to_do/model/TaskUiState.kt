package com.qadri.to_do.model

import com.qadri.to_do.ui.homescreen.TaskDetails

data class TaskUiState(
    val taskdetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)