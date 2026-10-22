package com.example.moviebox

import com.example.moviebox.data.AuthApi
import com.example.moviebox.data.AuthRetrofit
import com.example.moviebox.data.PasswordLoginRequest
import com.example.moviebox.data.User
import com.example.moviebox.ui.AuthState
import com.example.moviebox.ui.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockApi: AuthApi
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockApi = mock()
        AuthRetrofit.api = mockApi
        viewModel = AuthViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoginSuccessAdmin() = runTest(testDispatcher) {
        val adminUser = User(1, "admin@test.com", "Admin", "ADMIN")
        whenever(mockApi.loginWithPassword(any())).thenReturn(adminUser)

        viewModel.loginWithPassword("admin@test.com", "password")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(AuthState.LOGGED_IN_ADMIN, viewModel.authState.value)
        assertEquals(adminUser, viewModel.currentUser.value)
    }

    @Test
    fun testLoginSuccessUser() = runTest(testDispatcher) {
        val normalUser = User(2, "user@test.com", "User", "USER")
        whenever(mockApi.loginWithPassword(any())).thenReturn(normalUser)

        viewModel.loginWithPassword("user@test.com", "password")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(AuthState.LOGGED_IN_USER, viewModel.authState.value)
        assertEquals(normalUser, viewModel.currentUser.value)
    }

    @Test
    fun testLoginFailure() = runTest(testDispatcher) {
        val errorResponse = Response.error<User>(401, "Unauthorized".toResponseBody("text/plain".toMediaType()))
        whenever(mockApi.loginWithPassword(any())).thenThrow(HttpException(errorResponse))

        viewModel.loginWithPassword("user@test.com", "wrong")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(AuthState.ERROR, viewModel.authState.value)
        assertEquals("Login failed: Invalid email or password.", viewModel.errorMessage.value)
    }

    @Test
    fun testLogout() {
        viewModel.logout()
        assertEquals(AuthState.LOGGED_OUT, viewModel.authState.value)
        assertEquals(null, viewModel.currentUser.value)
    }
}
