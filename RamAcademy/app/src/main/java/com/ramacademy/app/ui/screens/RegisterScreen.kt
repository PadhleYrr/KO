package com.ramacademy.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramacademy.app.ui.Screen
import com.ramacademy.app.ui.components.*
import com.ramacademy.app.ui.theme.*
import com.ramacademy.app.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    vm: AuthViewModel = hiltViewModel()
) {
    val state by vm.authState.collectAsState()
    var name     by remember { mutableStateOf("") }
    var phone    by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm  by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, NavyLight)))
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Spacer(Modifier.height(20.dp))

            Text("🎓", fontSize = 48.sp)
            Spacer(Modifier.height(8.dp))
            Text("Ram Academy", style = MaterialTheme.typography.displayMedium, color = Color.White)
            Text("Create your account", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.65f))

            Spacer(Modifier.height(28.dp))

            Card(
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("New Registration", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(18.dp))

                    listOf(
                        Triple("Full Name",  Icons.Default.Person,   name),
                        Triple("Phone",      Icons.Default.Phone,    phone),
                        Triple("Email",      Icons.Default.Email,    email),
                    ).forEachIndexed { i, (label, icon, value) ->
                        OutlinedTextField(
                            value         = value,
                            onValueChange = {
                                when (i) { 0 -> name = it; 1 -> phone = it; 2 -> email = it }
                            },
                            label         = { Text(label) },
                            leadingIcon   = { Icon(icon, null, tint = Navy) },
                            singleLine    = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = when (i) {
                                    1 -> KeyboardType.Phone
                                    2 -> KeyboardType.Email
                                    else -> KeyboardType.Text
                                },
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape    = RoundedCornerShape(12.dp),
                            colors   = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Navy, focusedLabelColor = Navy, cursorColor = Navy
                            )
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    OutlinedTextField(
                        value         = password,
                        onValueChange = { password = it },
                        label         = { Text("Password") },
                        leadingIcon   = { Icon(Icons.Default.Lock, null, tint = Navy) },
                        trailingIcon  = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = TextMuted)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine    = true,
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Navy, focusedLabelColor = Navy, cursorColor = Navy
                        )
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value         = confirm,
                        onValueChange = { confirm = it },
                        label         = { Text("Confirm Password") },
                        leadingIcon   = { Icon(Icons.Default.LockReset, null, tint = Navy) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine    = true,
                        isError       = confirm.isNotEmpty() && confirm != password,
                        supportingText = {
                            if (confirm.isNotEmpty() && confirm != password)
                                Text("Passwords don't match", color = Danger)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Navy, focusedLabelColor = Navy, cursorColor = Navy
                        )
                    )

                    if (state.error != null) {
                        Spacer(Modifier.height(8.dp))
                        Surface(color = Danger.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Text(state.error!!, style = MaterialTheme.typography.bodySmall, color = Danger, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    NavyButton(
                        text    = "Create Account",
                        onClick = {
                            vm.register(name.trim(), phone.trim(), email.trim(), password)
                        },
                        loading  = state.loading,
                        enabled  = name.isNotBlank() && phone.isNotBlank() && password.isNotBlank() && confirm == password,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(14.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text("Already have an account?", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                        TextButton(onClick = { navController.popBackStack() }) {
                            Text("Login", color = Navy, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
