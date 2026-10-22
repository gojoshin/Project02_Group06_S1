package com.example.moviebox.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebox.data.AuthRetrofit
import com.example.moviebox.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = AuthRetrofit.api.listUsers()
                _users.value = response.content
            } catch (e: Exception) {
                _error.value = "Failed to fetch users: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            try {
                AuthRetrofit.api.deleteUser(userId)
                fetchUsers() // Refresh list
            } catch (e: Exception) {
                _error.value = "Failed to delete user: ${e.message}"
            }
        }
    }

    fun promoteToAdmin(userId: Long) {
        viewModelScope.launch {
            try {
                AuthRetrofit.api.updateUserRole(userId, mapOf("role" to "ADMIN"))
                fetchUsers() // Refresh list
            } catch (e: Exception) {
                _error.value = "Failed to promote user: ${e.message}"
            }
        }
    }
}
