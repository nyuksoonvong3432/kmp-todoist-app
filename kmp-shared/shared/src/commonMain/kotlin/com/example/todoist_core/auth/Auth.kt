package com.example.todoist_core.auth

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

    fun getAuthorizationUrl(): Url {
        return Url("$baseOAuthUrl/authorize?client_id=$clientID&scope=data:read,data:delete&state=asdasndakj")
    }

    @Throws(Throwable::class)
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
    }
}
