package com.example.testing

import com.example.todoist_core.AuthKoinHelper
import com.example.todoist_core.KoinHelper
import kotlin.test.Test
import kotlin.test.assertTrue

class KoinTest {
    @Test
    fun testShouldAbleToInitKoin() {
        val koinHelper = KoinHelper()
        koinHelper.initKoin()
        assertTrue(message = "Koin should be initialized") { true }
    }

    @Test
    fun testShouldAbleToGetAuthURL() {
        val koinHelper = KoinHelper()
        try {
            koinHelper.initKoin()
        } catch (e: Exception) { }
        val authKoinHelper = AuthKoinHelper()
        val url = authKoinHelper.getAuthorizationUrl()
        println("Authorization URL --> $url")
        assertTrue(message = "URL should not be null") { url != null }
    }
}