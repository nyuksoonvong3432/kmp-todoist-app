package com.example.todoist_core.auth

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AuthAuthenticatePayloadDTO(
    @SerialName("client_id")
    val clientID: String,
    @SerialName("client_secret")
    val clientSecret: String,
    @SerialName("code")
    val code: String,
    @SerialName("redirect_uri")
    val redirectUri: String
)

@Serializable
data class AuthAuthenticatedResponseDTO(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("token_type")
    val tokenType: String
)
