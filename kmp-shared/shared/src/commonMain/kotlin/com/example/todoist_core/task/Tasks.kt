package com.example.todoist_core.task

import com.example.todoist_core.api.APIClient
import com.example.todoist_core.auth.Authentication
import kotlin.concurrent.Volatile

class Tasks private constructor() {
    companion object {
        val instance: Tasks by lazy { Tasks() }
    }

    private val api = APIClient.instance

    suspend fun create(payload: TaskCreatePayloadDTO): TaskEntityDTO {
        return api.post(path = "/tasks", body = payload)
    }

    suspend fun getAll(): List<TaskEntityDTO> {
        return api.get(path = "/tasks")
    }
}