package com.example.moviebox

import com.example.moviebox.data.AuthApi
import com.example.moviebox.data.PasswordLoginRequest
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var authApi: AuthApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        authApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testLoginParsesResponseCorrectly() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "id": 1,
                    "email": "test@test.com",
                    "displayName": "Tester",
                    "role": "USER"
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        val response = authApi.loginWithPassword(PasswordLoginRequest("test@test.com", "pass"))
        
        assertEquals(1, response.id)
        assertEquals("test@test.com", response.email)
        assertEquals("Tester", response.displayName)
        assertEquals("USER", response.role)
    }
}
