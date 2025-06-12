package com.example.todoist_core.api

import com.example.todoist_core.auth.Authentication
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.concurrent.Volatile

class APIClient private constructor() {
    val baseUrl: Url = Url(urlString = "https://api.todoist.com/api/v1")

    val httpClient = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }
    val authentication = Authentication.getInstance()

    companion object {
        @Volatile
        private var instance: APIClient? = null

        fun getInstance(): APIClient {
            if (instance == null) {
                this.instance = APIClient()
            }
            return this.instance!!
        }
    }

    suspend inline fun <reified Body, reified Result> post(baseUrl: Url? = null, path: String, body: Body? = null): Result {
        val response = httpClient.post("${baseUrl ?: this.baseUrl}/$path") {
            headers {
                if (authentication.accessTokenType != null && authentication.accessToken != null) {
                    val token = "${authentication.accessTokenType} ${authentication.accessToken}"
                    append("Authorization", token)
                }
            }
            setBody(body)
        }
        return response.body()
    }

    suspend inline fun <reified Result> get(baseUrl: Url? = null, path: String, parameters: Parameters? = null): Result {
        val response = httpClient.get("${baseUrl ?: this.baseUrl}/$path") {
            headers {
                if (authentication.accessTokenType != null && authentication.accessToken != null) {
                    val token = "${authentication.accessTokenType} ${authentication.accessToken}"
                    append("Authorization", token)
                }
            }
            parameters?.forEach { key, values ->
                values.forEach { value ->
                    parameter(key, value)
                }
            }
        }
        return response.body()
    }
}
