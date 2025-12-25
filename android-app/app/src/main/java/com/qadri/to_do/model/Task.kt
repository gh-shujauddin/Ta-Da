package com.qadri.to_do.model

data class Task(
    val id: Long = 0,
    val taskName: String = "",
    val taskDescription: String = "",
    val isCompleted: Boolean = false
)