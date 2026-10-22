package com.example.moviebox.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.moviebox.R
import com.example.moviebox.ui.theme.Display
import com.example.moviebox.ui.theme.Mono
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onGoogleTokenReceived: (String) -> Unit,
    onPasswordLogin: (String, String) -> Unit = { _, _ -> },
    onPasswordRegister: (String, String, String) -> Unit = { _, _, _ -> },
    externalErrorMessage: String? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isCreateAccountMode by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    val displayedError = externalErrorMessage ?: localErrorMessage

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

        Spacer(modifier = Modifier.height(24.dp))

        // Toggle Tabs between Sign In and Create Account
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = { 
                isCreateAccountMode = false
                localErrorMessage = null
            }) {
                Text(
                    text = "SIGN IN",
                    fontFamily = Mono,
                    fontWeight = if (!isCreateAccountMode) FontWeight.Bold else FontWeight.Normal,
                    color = if (!isCreateAccountMode) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(onClick = { 
                isCreateAccountMode = true
                localErrorMessage = null
            }) {
                Text(
                    text = "CREATE ACCOUNT",
                    fontFamily = Mono,
                    fontWeight = if (isCreateAccountMode) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCreateAccountMode) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("EMAIL / USERNAME", fontFamily = Mono, fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (isCreateAccountMode) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("DISPLAY NAME", fontFamily = Mono, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("PASSWORD", fontFamily = Mono, fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                localErrorMessage = null
                if (isCreateAccountMode) {
                    onPasswordRegister(email, displayName, password)
                } else {
                    onPasswordLogin(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = email.isNotBlank() && password.isNotBlank() && (!isCreateAccountMode || displayName.isNotBlank())
        ) {
            Text(
                text = if (isCreateAccountMode) "CREATE ACCOUNT" else "LOG IN",
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

        Spacer(modifier = Modifier.height(16.dp))

        // Google Sign-in Button
        OutlinedButton(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    localErrorMessage = null

                    try {
                        val credentialManager = CredentialManager.create(context)
                        val googleOption = GetSignInWithGoogleOption.Builder(
                            serverClientId = context.getString(R.string.google_web_client_id)
                        ).build()

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleOption)
                            .build()

                        val result = credentialManager.getCredential(context = context, request = request)
                        val credential = result.credential

                        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                            Log.d("GoogleSignIn", "Google authentication successful")
                            onGoogleTokenReceived(googleCredential.idToken)
                        } else {
                            localErrorMessage = "Google sign-in failed."
                        }
                    } catch (e: Exception) {
                        Log.e("GoogleSignIn", "Google sign-in error", e)
                        localErrorMessage = "Google sign-in failed: ${e.message}"
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

        Spacer(modifier = Modifier.height(12.dp))

        // Dev / Test Bypass Button
        TextButton(
            onClick = { onGoogleTokenReceived("mock_dev_token") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "DEV / TEST BYPASS LOGIN (ADMIN)",
                fontFamily = Mono,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        displayedError?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }
    }
}
