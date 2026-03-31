package com.ramacademy.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramacademy.app.ui.Screen
import com.ramacademy.app.ui.components.*
import com.ramacademy.app.ui.theme.*
import com.ramacademy.app.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

// ─── Splash ───────────────────────────────────────────────────────────────────
@Composable
fun SplashScreen(navController: NavController, vm: AuthViewModel = hiltViewModel()) {
    val scale = remember { Animatable(0.6f) }
    val state by vm.authState.collectAsState()

    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        delay(1200)
        navController.navigate(if (state.isLoggedIn) Screen.Home.route else Screen.Login.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier        = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, NavyLight, Color(0xFF3949AB)))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier            = Modifier.scale(scale.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🎓", fontSize = 72.sp)
            Spacer(Modifier.height(16.dp))
            Text(
                "Ram Academy",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White
            )
            Text(
                "Prepare. Perform. Prevail.",
                style = MaterialTheme.typography.bodyLarge,
                color = GoldLight
            )
            Spacer(Modifier.height(48.dp))
            CircularProgressIndicator(color = Color.White.copy(alpha = 0.5f), strokeWidth = 2.dp)
        }
    }
}

// ─── OTP Verify ──────────────────────────────────────────────────────────────
@Composable
fun OtpVerifyScreen(navController: NavController, phone: String, vm: AuthViewModel = hiltViewModel()) {
    var otp by remember { mutableStateOf("") }
    val state by vm.authState.collectAsState()
    var resendTimer by remember { mutableStateOf(30) }

    LaunchedEffect(Unit) {
        while (resendTimer > 0) {
            delay(1000)
            resendTimer--
        }
    }
    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Navy, NavyLight)))) {
        Column(
            modifier            = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Spacer(Modifier.height(60.dp))
            Text("📱", fontSize = 60.sp)
            Spacer(Modifier.height(16.dp))
            Text("Verify Phone", style = MaterialTheme.typography.displayMedium, color = Color.White)
            Text("OTP sent to $phone", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.65f))

            Spacer(Modifier.height(36.dp))

            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color.White)) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value         = otp,
                        onValueChange = { if (it.length <= 6) otp = it },
                        label         = { Text("Enter 6-digit OTP") },
                        leadingIcon   = { Icon(Icons.Default.Pin, null, tint = Navy) },
                        singleLine    = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = OutlinedTextFieldDefaults.colors(focusedBorderColor = Navy, cursorColor = Navy)
                    )
                    Spacer(Modifier.height(16.dp))
                    NavyButton(
                        text     = "Verify OTP",
                        onClick  = { vm.verifyOtp(phone, otp) },
                        loading  = state.loading,
                        enabled  = otp.length == 6,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    if (resendTimer > 0) {
                        Text("Resend OTP in ${resendTimer}s", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    } else {
                        TextButton(onClick = { vm.resendOtp(phone); resendTimer = 30 }) {
                            Text("Resend OTP", color = Saff)
                        }
                    }
                }
            }
        }
    }
}

// ─── Forgot Password ──────────────────────────────────────────────────────────
@Composable
fun ForgotPasswordScreen(navController: NavController, vm: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var sent  by remember { mutableStateOf(false) }
    val state by vm.authState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Navy, NavyLight)))) {
        Column(
            modifier            = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Spacer(Modifier.height(60.dp))
            Text(if (sent) "✅" else "🔐", fontSize = 60.sp)
            Spacer(Modifier.height(16.dp))
            Text(
                if (sent) "Email Sent!" else "Forgot Password",
                style = MaterialTheme.typography.displayMedium, color = Color.White
            )
            Text(
                if (sent) "Check your inbox for reset link" else "We'll send a reset link to your email",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.65f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(36.dp))

            if (!sent) {
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color.White)) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedTextField(
                            value         = email,
                            onValueChange = { email = it },
                            label         = { Text("Email Address") },
                            leadingIcon   = { Icon(Icons.Default.Email, null, tint = Navy) },
                            singleLine    = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape    = RoundedCornerShape(12.dp),
                            colors   = OutlinedTextFieldDefaults.colors(focusedBorderColor = Navy, cursorColor = Navy)
                        )
                        Spacer(Modifier.height(16.dp))
                        NavyButton(
                            text     = "Send Reset Link",
                            onClick  = { vm.resetPassword(email.trim()); sent = true },
                            loading  = state.loading,
                            enabled  = email.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(10.dp))
                        TextButton(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                            Text("Back to Login", color = TextMuted)
                        }
                    }
                }
            } else {
                NavyButton("Back to Login", onClick = { navController.navigate(Screen.Login.route) { popUpTo(0) } }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
