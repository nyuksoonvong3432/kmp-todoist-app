package com.example.todoist_core.task

import com.example.todoist_core.api.APIClient
import kotlin.concurrent.Volatile

class Tasks private constructor() {
    companion object {
        @Volatile
        private var instance: Tasks? = null

        fun getInstance(): Tasks {
            if (instance == null) {
                this.instance = Tasks()
            }
            return this.instance!!
        }
    }

    private val api = APIClient.getInstance()

    suspend fun create(payload: TaskCreatePayloadDTO): TaskEntityDTO {
        return api.post(path = "/tasks", body = payload)
    }

    suspend fun getAll(): List<TaskEntityDTO> {
        return api.get(path = "/tasks")
    }
}