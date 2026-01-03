package com.example.lntu_app_1.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lntu_app_1.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState

    fun signIn(context: Context, clientId: String) {
        viewModelScope.launch {
            _uiState.value = LoginState.Loading

            val result = repository.signInWithGoogle(context, clientId)

            _uiState.value =
                if (result.isSuccess) LoginState.Success
                else LoginState.Error(result.exceptionOrNull()?.message ?: "Error")
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
