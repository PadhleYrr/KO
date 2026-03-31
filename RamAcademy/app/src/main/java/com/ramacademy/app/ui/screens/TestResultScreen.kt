package com.ramacademy.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramacademy.app.ui.Screen
import com.ramacademy.app.ui.components.*
import com.ramacademy.app.ui.theme.*
import com.ramacademy.app.viewmodel.TestViewModel

@Composable
fun TestResultScreen(
    navController: NavController,
    testId: String,
    vm: TestViewModel = hiltViewModel()
) {
    val result  by vm.result.collectAsState()
    val state   by vm.testState.collectAsState()
    var tab     by remember { mutableIntStateOf(0) }

    LaunchedEffect(testId) { vm.loadResult(testId) }

    Scaffold(
        topBar = {
            RamTopBar(
                title  = "Test Result",
                onBack = {
                    navController.navigate(Screen.Home.route) { popUpTo(0) }
                }
            )
        }
    ) { padding ->
        result?.let { r ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Score header
                Surface(
                    color  = Navy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier            = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier        = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(5.dp, GoldLight, CircleShape),
                            contentAlignment= Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "${r.score.toInt()}%",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = GoldLight
                                )
                                Text(
                                    gradeLabel(r.score),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            motivationalText(r.score),
                            style     = MaterialTheme.typography.titleLarge,
                            color     = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))

                        // Stat grid
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ResultStat("Correct",  "${r.correct}",  Success,  Modifier.weight(1f))
                            ResultStat("Wrong",    "${r.wrong}",    Danger,   Modifier.weight(1f))
                            ResultStat("Skipped",  "${r.skipped}",  Warn,     Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ResultStat("Rank",        "#${r.rank}",            GoldLight, Modifier.weight(1f))
                            ResultStat("Percentile",  "${r.percentile}%",      Navy.copy(alpha=0f), Modifier.weight(1f))
                            val mins = r.timeTaken / 60
                            val secs = r.timeTaken % 60
                            ResultStat("Time",        "%02d:%02d".format(mins,secs), TextMuted, Modifier.weight(1f))
                        }
                    }
                }

                // Tabs
                TabRow(
                    selectedTabIndex = tab,
                    containerColor   = Color.White,
                    contentColor     = Navy
                ) {
                    Tab(selected = tab == 0, onClick = { tab = 0 }) {
                        Text("Review Answers", modifier = Modifier.padding(vertical = 14.dp))
                    }
                    Tab(selected = tab == 1, onClick = { tab = 1 }) {
                        Text("Analysis", modifier = Modifier.padding(vertical = 14.dp))
                    }
                }

                when (tab) {
                    0 -> {
                        // Review
                        Column(
                            modifier              = Modifier.padding(16.dp),
                            verticalArrangement   = Arrangement.spacedBy(10.dp)
                        ) {
                            state.questions.forEachIndexed { idx, q ->
                                val selected = r.answers[q.id]
                                val isCorrect = selected == q.correctIndex
                                val isSkipped = selected == null
                                val borderColor = when {
                                    isSkipped -> Warn
                                    isCorrect -> Success
                                    else      -> Danger
                                }
                                Surface(
                                    shape  = RoundedCornerShape(12.dp),
                                    border = BorderStroke(2.dp, borderColor),
                                    color  = borderColor.copy(alpha = 0.05f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Surface(color = borderColor, shape = RoundedCornerShape(6.dp)) {
                                                Text(
                                                    when { isSkipped -> "S"; isCorrect -> "✓"; else -> "✗" },
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                            Text("Q${idx + 1}", style = MaterialTheme.typography.labelLarge, color = TextMuted)
                                        }
                                        Spacer(Modifier.height(6.dp))
                                        Text(q.text, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                        Spacer(Modifier.height(8.dp))
                                        Text(
                                            "✅ Correct: ${q.options.getOrElse(q.correctIndex) { "" }}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Success,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        if (!isCorrect && !isSkipped && selected != null) {
                                            Text(
                                                "❌ Your answer: ${q.options.getOrElse(selected) { "" }}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Danger
                                            )
                                        }
                                        if (q.explanation.isNotEmpty()) {
                                            Spacer(Modifier.height(6.dp))
                                            Surface(color = Color(0xFFF0FDF4), shape = RoundedCornerShape(8.dp)) {
                                                Text(
                                                    "💡 ${q.explanation}",
                                                    style    = MaterialTheme.typography.bodySmall,
                                                    color    = Color(0xFF15803D),
                                                    modifier = Modifier.padding(10.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        // Analysis
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            AnalysisBar("Accuracy",     r.score / 100f,              Saff)
                            AnalysisBar("Completion",   r.attempted.toFloat() / r.totalQuestions, Navy)
                            AnalysisBar("Speed Score",  minOf(1f, 1800f / r.timeTaken.coerceAtLeast(1)), Gold)
                        }
                    }
                }

                // Action buttons
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    NavyButton("Retake Test", onClick = { navController.navigate(Screen.TestAttempt.createRoute(testId)) }, modifier = Modifier.fillMaxWidth(), icon = Icons.Default.Refresh)
                    OutlinedButton(
                        onClick  = { navController.navigate(Screen.Home.route) { popUpTo(0) } },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape    = RoundedCornerShape(12.dp)
                    ) {
                        Text("Back to Home")
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        } ?: Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Navy)
        }
    }
}

@Composable
fun ResultStat(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        color  = Color(0xFF1E3A8A).copy(alpha = 0.2f),
        shape  = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.headlineMedium, color = color.takeIf { it != Navy.copy(alpha=0f) } ?: GoldLight, fontWeight = FontWeight.ExtraBold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.65f))
        }
    }
}

@Composable
fun AnalysisBar(label: String, progress: Float, color: Color) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.titleSmall)
            Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress   = { progress },
            modifier   = Modifier.fillMaxWidth().height(8.dp).clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp)),
            color      = color,
            trackColor = BorderLight
        )
    }
}

private fun gradeLabel(score: Float) = when {
    score >= 90 -> "Excellent"
    score >= 75 -> "Great"
    score >= 60 -> "Good"
    score >= 45 -> "Average"
    else        -> "Needs Work"
}

private fun motivationalText(score: Float) = when {
    score >= 90 -> "Outstanding performance! 🏆"
    score >= 75 -> "Well done! Keep it up 💪"
    score >= 60 -> "Good effort! Practice more 📚"
    score >= 45 -> "Don't give up, try again! 🔄"
    else        -> "Keep studying, you'll improve! 🎯"
}
