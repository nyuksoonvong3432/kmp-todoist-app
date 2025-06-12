package com.example.todoist_core.task

import com.example.todoist_core.api.APIClient
import com.example.todoist_core.api.APIEntities

class Tasks(private val api: APIClient) {
    @Throws(Throwable::class)
    suspend fun create(payload: TaskCreatePayloadDTO): TaskEntityDTO {
        return api.post(path = "/tasks", body = payload)
    }

    @Throws(Throwable::class)
    suspend fun getAll(): List<TaskEntityDTO> {
        val response: APIEntities<TaskEntityDTO> = api.get(path = "/tasks")
        return response.results
    }
}