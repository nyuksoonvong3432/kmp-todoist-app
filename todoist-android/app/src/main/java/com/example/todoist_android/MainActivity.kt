package com.example.todoist_android

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoist_android.ui.theme.TodoistandroidTheme
import com.example.todoist_core.AuthKoinHelper
import com.example.todoist_core.KoinHelper

class MainActivity : ComponentActivity() {

    val authKoinHelper = AuthKoinHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        KoinHelper().initKoin()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoistandroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                        )
                        Row {
                            Image(
                                painter = painterResource(R.drawable.ic_launcher_background),
                                contentDescription = "Some image"
                            )
                        }
                        Button(onClick = { onGrantAccessBtnTapped() }) {
                            Text("Grant access")
                        }
                    }
                }
            }
        }
    }

    private fun onGrantAccessBtnTapped() {
        try {
            val authorizationUrl = authKoinHelper.getAuthorizationUrl()
            Toast.makeText(this, "URL: $authorizationUrl", Toast.LENGTH_SHORT).show()
        } catch(e: Throwable) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoistandroidTheme {
        Greeting("Android")
    }
}