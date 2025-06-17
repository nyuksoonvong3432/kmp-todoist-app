package com.example.todoist_core.auth

import com.example.todoist_core.cache.CacheContainerKey
import com.example.todoist_core.cache.getCacheContainer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Authentication {
    private val baseOAuthUrl = "https://todoist.com/oauth"
    private val clientID = "19530b5a25ff4321ad889abc37bc7134"

    var accessToken: String? = null
        private set
    var accessTokenType: String? = null
        private set

    suspend fun tryRestoreAccess() {
        loadTokenFromCache()
        if (accessToken == null || accessTokenType == null) {
            throw Exception("No token found.")
        }
    }

    fun getAuthorizationUrl(): Url {
        return Url("$baseOAuthUrl/authorize?client_id=$clientID&scope=data:delete,data:read_write&state=asdasndakj")
    }

    suspend fun authenticate(clientSecret: String, code: String, redirectUrlPath: String) {
        val httpClient = HttpClient() {
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
        val result: AuthAuthenticatedResponseDTO =
            httpClient.post(urlString = "$baseOAuthUrl/access_token") {
                contentType(ContentType.Application.Json)
                setBody(
                    AuthAuthenticatePayloadDTO(
                        clientID = clientID,
                        clientSecret = clientSecret,
                        code = code,
                        redirectUrlPath
                    )
                )
            }.body()
        this.accessToken = result.accessToken
        this.accessTokenType = result.tokenType
        saveTokenInCache("${result.tokenType} ${result.accessToken}")
    }

    suspend fun logout() {
        clearTokenFromCache()
        this.accessToken = null
        this.accessTokenType = null
    }

    private suspend fun saveTokenInCache(token: String) {
        getCacheContainer().write(CacheContainerKey.ACCESS_TOKEN, token)
    }

    private suspend fun loadTokenFromCache() {
        val result: String = getCacheContainer().read(CacheContainerKey.ACCESS_TOKEN)
            ?: return

        val splitted = result.split(" ")
        val tokenType = splitted.getOrNull(0)
        val token = splitted.getOrNull(1)
        if (tokenType != null && token != null) {
            this.accessToken = token
            this.accessTokenType = tokenType
        }
    }

    private suspend fun clearTokenFromCache() {
        getCacheContainer().remove(CacheContainerKey.ACCESS_TOKEN)
    }
}
