package com.qadri.to_do.model.dto

data class TaskDto(
    val id: Long,
    val taskName: String,
    val taskDescription: String,
    val isCompleted: Boolean,
    val isDeleted: Boolean,
    val lastUpdateTime: Long,
    val createdAt: Long
)
