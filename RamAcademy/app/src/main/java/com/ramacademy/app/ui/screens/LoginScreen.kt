package com.ramacademy.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramacademy.app.ui.Screen
import com.ramacademy.app.ui.components.*
import com.ramacademy.app.ui.theme.*
import com.ramacademy.app.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    vm: AuthViewModel = hiltViewModel()
) {
    val state by vm.authState.collectAsState()
    val focusManager = LocalFocusManager.current
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, NavyLight, Color(0xFF3949AB))))
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Spacer(Modifier.height(40.dp))

            // Logo
            Text("🎓", fontSize = 60.sp)
            Spacer(Modifier.height(12.dp))
            Text(
                "Ram Academy",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
            Text(
                "Prepare. Perform. Prevail.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.65f)
            )

            Spacer(Modifier.height(40.dp))

            // Card
            Card(
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "Welcome Back",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Text(
                        "Login to your account",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                    Spacer(Modifier.height(20.dp))

                    // Phone / Email
                    OutlinedTextField(
                        value         = phone,
                        onValueChange = { phone = it },
                        label         = { Text("Mobile / Email") },
                        leadingIcon   = { Icon(Icons.Default.Person, null, tint = Navy) },
                        singleLine    = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction    = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = Navy,
                            focusedLabelColor    = Navy,
                            cursorColor          = Navy
                        )
                    )
                    Spacer(Modifier.height(14.dp))

                    // Password
                    OutlinedTextField(
                        value         = password,
                        onValueChange = { password = it },
                        label         = { Text("Password") },
                        leadingIcon   = { Icon(Icons.Default.Lock, null, tint = Navy) },
                        trailingIcon  = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    null,
                                    tint = TextMuted
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                                               else PasswordVisualTransformation(),
                        singleLine    = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Navy,
                            focusedLabelColor  = Navy,
                            cursorColor        = Navy
                        )
                    )

                    // Forgot password
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        TextButton(onClick = { navController.navigate(Screen.ForgotPassword.route) }) {
                            Text("Forgot Password?", color = Saff, style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    Spacer(Modifier.height(6.dp))

                    // Error
                    if (state.error != null) {
                        Surface(
                            color  = Danger.copy(alpha = 0.1f),
                            shape  = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                state.error!!,
                                style    = MaterialTheme.typography.bodySmall,
                                color    = Danger,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                    }

                    NavyButton(
                        text     = "Login",
                        onClick  = { vm.login(phone.trim(), password) },
                        loading  = state.loading,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(14.dp))

                    // Register link
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text("Don't have an account?", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
                            Text("Register", color = Navy, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                "By continuing you agree to our Terms & Privacy Policy",
                style     = MaterialTheme.typography.bodySmall,
                color     = Color.White.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}
