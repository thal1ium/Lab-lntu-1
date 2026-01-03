package com.example.lntu_app_1.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lntu_app_1.viewmodel.HomeViewModel
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val lastMessage by viewModel.lastMessage.collectAsState()
    var messageText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadLastMessage()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Main") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Exit")
                    }
                }
            )
        }
    ) { padding ->
        Column(
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
                        viewModel.sendMessage(messageText)
                        messageText = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send")
            }

            Divider()

            Text("Last message:", style = MaterialTheme.typography.titleMedium)
            Text(lastMessage, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
