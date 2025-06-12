package com.example.todoist_core.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class APIEntities<T>(
    val results: List<T>,
    @SerialName("next_cursor")
    val nextCursor: String?
)
