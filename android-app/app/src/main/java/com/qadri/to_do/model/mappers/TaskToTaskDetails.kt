package com.qadri.to_do.model.mappers

import com.qadri.to_do.model.Task
import com.qadri.to_do.ui.homescreen.TaskDetails

fun Task.toTaskDetails(): TaskDetails = TaskDetails(
    id = id,
    taskName = taskName,
    taskDescription = taskDescription,
    isCompleted = isCompleted
)