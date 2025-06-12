package com.example.todoist_core.task

import com.example.todoist_core.api.APIClient

class Tasks(private val api: APIClient) {
    @Throws(Throwable::class)
    suspend fun create(payload: TaskCreatePayloadDTO): TaskEntityDTO {
        return api.post(path = "/tasks", body = payload)
    }

    @Throws(Throwable::class)
    suspend fun getAll(): List<TaskEntityDTO> {
        return api.get(path = "/tasks")
    }
}