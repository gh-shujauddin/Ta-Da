package com.qadri.to_do.model.dto

import java.time.LocalDateTime

data class TaskDto(
    val id: Long,
    val taskName: String,
    val taskDescription: String,
    val isCompleted: Boolean,
    val lastUpdateTime: LocalDateTime,
    val createdAt: LocalDateTime
)
