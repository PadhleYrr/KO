package com.ramacademy.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramacademy.app.ui.Screen
import com.ramacademy.app.ui.components.*
import com.ramacademy.app.ui.theme.*
import com.ramacademy.app.viewmodel.TestViewModel
import kotlinx.coroutines.delay

@Composable
fun TestAttemptScreen(
    navController: NavController,
    testId: String,
    vm: TestViewModel = hiltViewModel()
) {
    val state       by vm.testState.collectAsState()
    var showSubmit  by remember { mutableStateOf(false) }
    var showQPanel  by remember { mutableStateOf(false) }

    LaunchedEffect(testId) { vm.loadTest(testId) }

    // Countdown timer
    LaunchedEffect(state.remainingSeconds) {
        if (state.remainingSeconds > 0) {
            delay(1000)
            vm.tickTimer()
        } else if (state.remainingSeconds == 0 && !state.isSubmitted) {
            vm.submit()
            navController.navigate(Screen.TestResult.createRoute(testId))
        }
    }

    BackHandler { showSubmit = true }

    if (showSubmit) {
        AlertDialog(
            onDismissRequest = { showSubmit = false },
            title   = { Text("Submit Test?") },
            text    = {
                Text("You have answered ${state.answeredCount} of ${state.questions.size} questions.\nAre you sure you want to submit?")
            },
            confirmButton = {
                Button(
                    onClick = { vm.submit(); navController.navigate(Screen.TestResult.createRoute(testId)) },
                    colors  = ButtonDefaults.buttonColors(containerColor = Navy)
                ) { Text("Submit") }
            },
            dismissButton = {
                TextButton(onClick = { showSubmit = false }) { Text("Continue") }
            }
        )
    }

    // Question panel overlay
    if (showQPanel) {
        QuestionPanelSheet(
            total     = state.questions.size,
            answered  = state.answers,
            flagged   = state.flagged,
            current   = state.currentIndex,
            onSelect  = { vm.jumpTo(it); showQPanel = false },
            onDismiss = { showQPanel = false }
        )
    }

    Scaffold(
        topBar = {
            Surface(color = Navy, shadowElevation = 4.dp) {
                Column {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                    Row(
                        modifier              = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { showSubmit = true }) {
                            Icon(Icons.Default.Close, null, tint = Color.White)
                        }
                        Text(
                            state.testTitle,
                            style    = MaterialTheme.typography.titleLarge,
                            color    = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        // Timer
                        val mins = state.remainingSeconds / 60
                        val secs = state.remainingSeconds % 60
                        Surface(
                            color  = if (state.remainingSeconds < 300) Danger else Color.White.copy(alpha = 0.2f),
                            shape  = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                "%02d:%02d".format(mins, secs),
                                style      = MaterialTheme.typography.titleMedium,
                                color      = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                modifier   = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                        IconButton(onClick = { showQPanel = true }) {
                            Icon(Icons.Default.GridView, null, tint = Color.White)
                        }
                    }
                    // Progress bar
                    val progress = if (state.questions.isEmpty()) 0f
                                   else (state.currentIndex + 1).toFloat() / state.questions.size
                    LinearProgressIndicator(
                        progress   = { progress },
                        modifier   = Modifier.fillMaxWidth().height(3.dp),
                        color      = GoldLight,
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )
                }
            }
        }
    ) { padding ->
        if (state.questions.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Navy)
            }
            return@Scaffold
        }

        val q = state.questions[state.currentIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Question meta
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Surface(color = Color(0xFFEEF0FF), shape = RoundedCornerShape(20.dp)) {
                        Text(
                            "Q ${state.currentIndex + 1} / ${state.questions.size}",
                            style      = MaterialTheme.typography.labelMedium,
                            color      = Navy,
                            fontWeight = FontWeight.Bold,
                            modifier   = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (q.chapter.isNotEmpty()) Chip(q.chapter, TextMuted)
                        Chip(q.difficulty,
                            when (q.difficulty) {
                                "Easy"   -> Success
                                "Hard"   -> Danger
                                else     -> Warn
                            }
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Question text
                Card(
                    shape  = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Text(
                        q.text,
                        style    = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(18.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Options
                val selectedAnswer = state.answers[q.id]
                q.options.forEachIndexed { idx, option ->
                    val isSelected = selectedAnswer == idx
                    val label = listOf("A", "B", "C", "D").getOrElse(idx) { "${idx+1}" }

                    Surface(
                        color  = if (isSelected) Color(0xFFEEF0FF) else Color.White,
                        shape  = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            2.dp,
                            if (isSelected) Navy else BorderLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clickable { vm.selectAnswer(q.id, idx) }
                    ) {
                        Row(
                            modifier          = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier        = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Navy else Color(0xFFEEF0FF)),
                                contentAlignment= Alignment.Center
                            ) {
                                Text(
                                    label,
                                    style      = MaterialTheme.typography.labelLarge,
                                    color      = if (isSelected) Color.White else Navy,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Text(option, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                            if (isSelected) {
                                Icon(Icons.Default.CheckCircle, null, tint = Navy, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Actions row
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    // Flag
                    val isFlagged = state.flagged.contains(q.id)
                    OutlinedButton(
                        onClick = { vm.toggleFlag(q.id) },
                        border  = BorderStroke(1.5.dp, if (isFlagged) Danger else BorderLight),
                        colors  = ButtonDefaults.outlinedButtonColors(contentColor = if (isFlagged) Danger else TextMuted)
                    ) {
                        Icon(if (isFlagged) Icons.Default.Flag else Icons.Default.FlagOutlined, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(if (isFlagged) "Flagged" else "Flag")
                    }

                    Spacer(Modifier.weight(1f))

                    // Prev
                    if (state.currentIndex > 0) {
                        OutlinedButton(onClick = { vm.prev() }) {
                            Icon(Icons.Default.ArrowBack, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Prev")
                        }
                    }

                    // Next / Submit
                    if (state.currentIndex < state.questions.size - 1) {
                        Button(
                            onClick = { vm.next() },
                            colors  = ButtonDefaults.buttonColors(containerColor = Navy)
                        ) {
                            Text("Next")
                            Spacer(Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        SaffButton("Submit Test", onClick = { showSubmit = true })
                    }
                }
            }
        }
    }
}

// ─── Question Panel Sheet ─────────────────────────────────────────────────────
@Composable
fun QuestionPanelSheet(
    total: Int,
    answered: Map<String, Int>,
    flagged: Set<String>,
    current: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape  = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            color  = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = false) {}
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Question Overview", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(6.dp))

                // Legend
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LegendDot(Navy, "Current")
                    LegendDot(Success, "Answered")
                    LegendDot(Danger, "Flagged")
                    LegendDot(BorderLight, "Unattempted")
                }

                Spacer(Modifier.height(14.dp))

                // Grid
                val cols = 5
                val rows = (total + cols - 1) / cols
                for (r in 0 until rows) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                        for (c in 0 until cols) {
                            val idx = r * cols + c
                            if (idx < total) {
                                val qId      = idx.toString()
                                val isCurrent  = idx == current
                                val isAnswered = answered.containsKey(qId)
                                val isFlagged  = flagged.contains(qId)
                                val bg = when {
                                    isCurrent  -> Navy
                                    isFlagged  -> Danger
                                    isAnswered -> Success
                                    else       -> Color(0xFFF1F5F9)
                                }
                                Box(
                                    modifier        = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(bg)
                                        .clickable { onSelect(idx) },
                                    contentAlignment= Alignment.Center
                                ) {
                                    Text(
                                        "${idx + 1}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (bg == Color(0xFFF1F5F9)) TextPrimary else Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                NavyButton("Close", onClick = onDismiss, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(3.dp)).background(color))
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
}
