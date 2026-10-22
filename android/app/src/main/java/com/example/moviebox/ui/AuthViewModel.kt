package com.example.moviebox.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebox.data.AuthRetrofit
import com.example.moviebox.data.LoginRequest
import com.example.moviebox.data.PasswordLoginRequest
import com.example.moviebox.data.PasswordRegisterRequest
import com.example.moviebox.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class AuthState {
    LOGGED_OUT, LOADING, LOGGED_IN_USER, LOGGED_IN_ADMIN, ERROR
}

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow(AuthState.LOGGED_OUT)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun authenticateWithGoogle(idToken: String) {
        _authState.value = AuthState.LOADING
        viewModelScope.launch {
            try {
                val user = AuthRetrofit.api.loginWithGoogle(LoginRequest(idToken))
                handleSuccessLogin(user)
            } catch (e: Exception) {
                _errorMessage.value = "Google login failed: ${e.message}"
                _authState.value = AuthState.ERROR
            }
        }
    }

    fun loginWithPassword(email: String, password: String) {
        _authState.value = AuthState.LOADING
        viewModelScope.launch {
            try {
                val user = AuthRetrofit.api.loginWithPassword(PasswordLoginRequest(email, password))
                handleSuccessLogin(user)
            } catch (e: Exception) {
                _errorMessage.value = "Login failed. Check email or password."
                _authState.value = AuthState.ERROR
            }
        }
    }

    fun registerWithPassword(email: String, displayName: String, password: String) {
        _authState.value = AuthState.LOADING
        viewModelScope.launch {
            try {
                val user = AuthRetrofit.api.registerWithPassword(PasswordRegisterRequest(email, displayName, password))
                handleSuccessLogin(user)
            } catch (e: Exception) {
                _errorMessage.value = "Registration failed: ${e.message}"
                _authState.value = AuthState.ERROR
            }
        }
    }

    private fun handleSuccessLogin(user: User) {
        _currentUser.value = user
        if (user.role == "ADMIN") {
            _authState.value = AuthState.LOGGED_IN_ADMIN
        } else {
            _authState.value = AuthState.LOGGED_IN_USER
        }
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.LOGGED_OUT
    }
}
