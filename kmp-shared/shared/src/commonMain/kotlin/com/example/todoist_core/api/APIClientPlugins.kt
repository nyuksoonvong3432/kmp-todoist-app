package com.example.todoist_core.api

import io.ktor.client.plugins.api.createClientPlugin

class TodoistAuthHeaderPluginConfig {
    var accessTokenType: String = ""
    var accessToken: String = ""
}

val TodoistAuthHeaderPlugin = createClientPlugin("TodoistAuthHeaderPlugin", ::TodoistAuthHeaderPluginConfig) {
    val accessTokenType = pluginConfig.accessTokenType
    val accessToken = pluginConfig.accessToken
    onRequest { request, content ->
        if (accessToken.isNotEmpty() && accessTokenType.isNotEmpty()) {
            val token = "$accessTokenType $accessToken"
            request.headers.append("Authorization", token)
        }
    }
}