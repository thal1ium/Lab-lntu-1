package com.example.lntu_app_1.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class MessagesRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getLastMessage(): String {
        val snapshot = db.collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        return snapshot.documents
            .firstOrNull()
            ?.getString("text")
            ?: "— no messages —"
    }

    suspend fun sendMessage(text: String) {
        val message = hashMapOf(
            "text" to text,
            "timestamp" to Timestamp.now()
        )
        db.collection("messages").add(message).await()
    }
}
