package com.example.todoist_core.task

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskCreatePayloadDTO(
    val content: String,
    val description: String?,
    @SerialName("project_id")
    val projectId: String?
)

@Serializable
data class TaskEntityDTO(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("project_id")
    val projectId: String,
    val description: String,
    val content: String,
    @SerialName("added_at")
    val addedAt: String
)
