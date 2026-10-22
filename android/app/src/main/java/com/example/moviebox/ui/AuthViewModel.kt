package com.example.moviebox.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebox.data.AuthRetrofit
import com.example.moviebox.data.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class AuthState {
    LOGGED_OUT, LOADING, LOGGED_IN_USER, LOGGED_IN_ADMIN, ERROR
}

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow(AuthState.LOGGED_OUT)
    val authState: StateFlow<AuthState> = _authState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun authenticateWithGoogle(idToken: String) {
        _authState.value = AuthState.LOADING
        viewModelScope.launch {
            try {
                val response = AuthRetrofit.api.loginWithGoogle(LoginRequest(idToken))
                if (response.role == "ADMIN") {
                    _authState.value = AuthState.LOGGED_IN_ADMIN
                } else {
                    _authState.value = AuthState.LOGGED_IN_USER
                }
            } catch (e: Exception) {
                _errorMessage.value = "Login failed: ${e.message}"
                _authState.value = AuthState.ERROR
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.LOGGED_OUT
    }
}
