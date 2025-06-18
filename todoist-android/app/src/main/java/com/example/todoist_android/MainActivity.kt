package com.example.todoist_android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoist_android.ui.theme.TodoistandroidTheme
import com.example.todoist_core.AuthKoinHelper
import com.example.todoist_core.KoinHelper
import com.example.todoist_core.TaskKoinHelper
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    val authKoinHelper = AuthKoinHelper()
    val taskKoinHelper = TaskKoinHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            KoinHelper().initKoin()
        } catch(e: Exception) {
            print("Koin already started")
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoistandroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Greeting(
                            name = "Android", modifier = Modifier.padding(innerPadding)
                        )
                        Row {
                            Image(
                                painter = painterResource(R.drawable.ic_launcher_background),
                                contentDescription = "Some image"
                            )
                        }

                        val uriHandler = LocalUriHandler.current
                        Button(onClick = { onGrantAccessBtnTapped(uriHandler) }) {
                            Text("Grant access")
                        }
                    }
                }
            }
        }
        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        println("Handle deeplink")
        if (intent.action == Intent.ACTION_VIEW) {
            intent.data?.let { uri ->
                val scheme = uri.scheme
                val code = uri.getQueryParameter("code")
                if (scheme != "todoist-app" || code == null) {
                    return
                }
                val clientSecret = BuildConfig.todoistClientSecret
                runBlocking {
                    authKoinHelper.authenticate(
                        clientSecret = clientSecret,
                        code = code,
                        redirectUrlPath = "com.example.todoist-ios://authorization"
                    )
                    loadTasks()
                }
            }
        }
    }

    private suspend fun loadTasks() {
        try {
            val tasks = taskKoinHelper.getAll()
            print("loaded tasks: $tasks")
        } catch (e: Throwable) {
            Toast.makeText(this, "Failed to load tasks: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onGrantAccessBtnTapped(uriHandler: UriHandler) {
        try {
            val authorizationUrl = authKoinHelper.getAuthorizationUrl()
            uriHandler.openUri(authorizationUrl.toString())
        } catch (e: Throwable) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoistandroidTheme {
        Greeting("Android")
    }
}