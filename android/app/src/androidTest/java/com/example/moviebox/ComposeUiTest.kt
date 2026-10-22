package com.example.moviebox

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.moviebox.ui.screens.LoginScreen
import com.example.moviebox.ui.theme.MovieBoxTheme
import org.junit.Rule
import org.junit.Test

class ComposeUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysExpectedElements() {
        composeTestRule.setContent {
            MovieBoxTheme {
                LoginScreen(
                    onGoogleTokenReceived = {},
                    onPasswordLogin = { _, _ -> },
                    onPasswordRegister = { _, _, _ -> }
                )
            }
        }

        // Check if important elements are on screen
        composeTestRule.onNodeWithText("Welcome to MovieBox").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign in with Google").assertIsDisplayed()
    }
}
