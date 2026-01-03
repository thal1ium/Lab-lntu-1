package com.example.lntu_app_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lntu_app_1.data.MessagesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val repository = MessagesRepository()

    private val _lastMessage = MutableStateFlow("— no messages —")
    val lastMessage: StateFlow<String> = _lastMessage

    fun loadLastMessage() {
        viewModelScope.launch {
            try {
                _lastMessage.value = repository.getLastMessage()
            } catch (e: Exception) {
                _lastMessage.value = "Error loading message"
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            repository.sendMessage(text)
            _lastMessage.value = text
        }
    }
}
