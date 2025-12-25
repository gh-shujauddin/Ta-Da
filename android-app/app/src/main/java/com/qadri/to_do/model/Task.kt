package com.qadri.to_do.model

data class Task(
    val id: Long,
    val taskName: String,
    val taskDescription: String,
    val isCompleted: Boolean,
)