package com.example.todoist_core

import com.example.todoist_core.api.APIClient
import com.example.todoist_core.auth.Authentication
import com.example.todoist_core.task.TaskCreatePayloadDTO
import com.example.todoist_core.task.TaskEntityDTO
import com.example.todoist_core.task.Tasks
import io.ktor.http.Url
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinHelper : KoinComponent {
    fun initKoin() {
        startKoin {
            modules(module {
                single<Authentication> { Authentication() }
                single<APIClient> { APIClient(authentication = get()) }
                single<Tasks> { Tasks(api = get()) }
            })
        }
    }
}

class TaskKoinHelper : KoinComponent {
    private val tasks: Tasks by inject()

    @Throws(Throwable::class)
    suspend fun create(payload: TaskCreatePayloadDTO): TaskEntityDTO = tasks.create(payload)

    @Throws(Throwable::class)
    suspend fun getAll(): List<TaskEntityDTO> = tasks.getAll()
}

class AuthKoinHelper : KoinComponent {
    private val authentication: Authentication by inject()

    @Throws(Throwable::class)
    suspend fun tryRestoreAccess() = authentication.tryRestoreAccess()

    fun getAuthorizationUrl(): Url {
        return authentication.getAuthorizationUrl()
    }

    @Throws(Throwable::class)
    suspend fun authenticate(clientSecret: String, code: String, redirectUrlPath: String) =
        authentication.authenticate(clientSecret, code, redirectUrlPath)

    @Throws(Throwable::class)
    suspend fun logout() = authentication.logout()
}