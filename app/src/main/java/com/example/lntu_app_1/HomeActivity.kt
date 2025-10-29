package com.example.lntu_app_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.tasks.await

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var messageText by remember { mutableStateOf("") }
    var lastMessage by remember { mutableStateOf("— no messages —") }

    LaunchedEffect(Unit) {
        val snapshot = db.collection("messages")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
        if (!snapshot.isEmpty) {
            lastMessage = snapshot.documents.first().getString("text") ?: "—"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Main") },
                actions = {
                    TextButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    }) {
                        Text("Exit")
                    }
                }
            )
        }
    ) { padding ->
        Column (
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text("Enter message") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        val message = hashMapOf(
                            "text" to messageText,
                            "timestamp" to com.google.firebase.Timestamp.now()
                        )
                        db.collection("messages")
                            .add(message)
                            .addOnSuccessListener {
                                lastMessage = messageText
                                messageText = ""
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send")
            }

            Divider()

            Text(
                text = "Last message:",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = lastMessage,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}