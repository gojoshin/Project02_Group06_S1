
package com.example.moviebox.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.moviebox.R
import com.example.moviebox.ui.theme.Display
import com.example.moviebox.ui.theme.Mono
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import android.util.Log

@Composable
fun LoginScreen(
    onGoogleTokenReceived: (String) -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "MovieBox",
            fontFamily = Display,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Login with Google",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = {
                coroutineScope.launch {

                    isLoading = true
                    errorMessage = null

                    try {
                        val credentialManager =
                            CredentialManager.create(context)

                        val googleOption =
                            GetSignInWithGoogleOption.Builder(
                                serverClientId = context.getString(
                                    R.string.google_web_client_id
                                )
                            ).build()

                        val request =
                            GetCredentialRequest.Builder()
                                .addCredentialOption(googleOption)
                                .build()

                        val result =
                            credentialManager.getCredential(
                                context = context,
                                request = request
                            )

                        val credential = result.credential

                        if (
                            credential is CustomCredential &&
                            credential.type ==
                            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                        ) {

                            val googleCredential =
                                GoogleIdTokenCredential.createFrom(
                                    credential.data
                                )

                            Log.d("GoogleSignIn", "Google authentication successful")
                            onGoogleTokenReceived(
                                googleCredential.idToken
                            )

                        } else {
                            errorMessage =
                                "Google sign-in failed."
                        }

                    } catch (e: Exception) {
                        android.util.Log.e(
                            "GoogleSignIn",
                            "Google sign-in error",
                            e
                        )

                        errorMessage =
                            "Google sign-in failed: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },

            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {

            Text(
                text = "LOGIN WITH GOOGLE",
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        if (isLoading) {
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            CircularProgressIndicator()
        }

        errorMessage?.let { message ->

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
