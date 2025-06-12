package com.example.todoist_core.auth

import com.example.todoist_core.api.APIClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Url
import kotlin.concurrent.Volatile

final class Authentication private constructor() {

    companion object {
        @Volatile
        private var instance: Authentication? = null

        fun getInstance(): Authentication {
            if (instance == null) {
                this.instance = Authentication()
            }
            return this.instance!!
        }
    }

    private val baseOAuthUrl = "https://todoist.com/oauth"
    private val api = APIClient.getInstance()
    private val clientID = "19530b5a25ff4321ad889abc37bc7134"

    var accessToken: String? = null
        private set
    var accessTokenType: String? = null
        private set

    fun setAccessToken(token: String, tokenType: String) {
        this.accessToken = token
        this.accessTokenType = tokenType
    }

    fun getAuthorizationUrl(): Url {
        return Url("$baseOAuthUrl/authorize?client_id=$clientID&scope=data:read,data:delete&state=asdasndakj")
    }

    suspend fun authenticate(state: String, code: String, redirectUrlPath: String) {
        val result: AuthAuthenticatedResponseDTO = api.post(
            baseUrl = Url(urlString = baseOAuthUrl),
            path = "/access_token",
            body = AuthAuthenticatePayloadDTO(
                clientID = clientID,
                clientSecret = state,
                code = code,
                redirectUrlPath
            ),
        )
        this.setAccessToken(result.accessToken, result.tokenType)
    }
}
