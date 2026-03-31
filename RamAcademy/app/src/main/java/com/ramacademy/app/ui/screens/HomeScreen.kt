package com.ramacademy.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramacademy.app.ui.Screen
import com.ramacademy.app.ui.components.*
import com.ramacademy.app.ui.theme.*
import com.ramacademy.app.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    Scaffold(
        bottomBar = {
            RamBottomNav(
                currentRoute = Screen.Home.route,
                onNavigate   = { navController.navigate(it) { launchSingleTop = true } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier            = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // ── Header (Navy gradient) ──────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(listOf(Navy, NavyLight))
                        )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "🙏 Jai Shri Ram",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GoldLight
                                )
                                Text(
                                    "Ram Academy",
                                    style = MaterialTheme.typography.displayMedium,
                                    color = Color.White
                                )
                                Text(
                                    "Prepare. Perform. Prevail.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.65f)
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                                    Icon(Icons.Default.Notifications, null, tint = Color.White)
                                }
                                IconButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                                    Icon(Icons.Default.ShoppingCart, null, tint = Color.White)
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Search bar
                        Surface(
                            color  = Color.White.copy(alpha = 0.15f),
                            shape  = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("store_search") }
                        ) {
                            Row(
                                modifier          = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Search, null, tint = Color.White.copy(alpha = 0.7f))
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    "Search courses, tests, notes…",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }

            // ── Quick Stats ────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        label      = "Courses",
                        value      = "${state.enrolledCount}",
                        accentColor= Navy,
                        icon       = Icons.Default.PlayCircle,
                        modifier   = Modifier.weight(1f)
                    )
                    StatCard(
                        label      = "Tests Done",
                        value      = "${state.testsDone}",
                        accentColor= Saff,
                        icon       = Icons.Default.Quiz,
                        modifier   = Modifier.weight(1f)
                    )
                    StatCard(
                        label      = "Points",
                        value      = "${state.points}",
                        accentColor= Gold,
                        icon       = Icons.Default.Stars,
                        modifier   = Modifier.weight(1f)
                    )
                }
            }

            // ── Streak ─────────────────────────────────────────────────────
            if (state.streak > 0) {
                item {
                    Spacer(Modifier.height(16.dp))
                    StreakBanner(state.streak)
                }
            }

            // ── Quick Action Grid ──────────────────────────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                SectionHeader(title = "Quick Access")
                Spacer(Modifier.height(10.dp))

                val quickActions = listOf(
                    Triple(Icons.Default.LiveTv,     "Live Classes",    Screen.LiveClasses.route),
                    Triple(Icons.Default.Quiz,        "Daily Quiz",      Screen.DailyQuiz.route),
                    Triple(Icons.Default.MenuBook,    "Study Material",  Screen.StudyMaterial.createRoute()),
                    Triple(Icons.Default.Newspaper,   "Current Affairs", Screen.CurrentAffairs.route),
                    Triple(Icons.Default.Schedule,    "Time Table",      Screen.TimeTable.route),
                    Triple(Icons.Default.HelpOutline, "Ask Doubt",       Screen.AddDoubt.route),
                    Triple(Icons.Default.Download,    "Downloads",       Screen.Downloads.route),
                    Triple(Icons.Default.People,      "Faculty",         Screen.Teachers.route),
                )

                LazyHorizontalGrid(
                    rows             = GridCells.Fixed(2),
                    modifier         = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement   = Arrangement.spacedBy(10.dp),
                    contentPadding   = PaddingValues(end = 10.dp)
                ) {
                    items(quickActions) { (icon, label, route) ->
                        QuickActionItem(icon, label) { navController.navigate(route) }
                    }
                }
            }

            // ── Continue Watching ──────────────────────────────────────────
            if (state.continueCourses.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(20.dp))
                    SectionHeader(
                        title       = "Continue Learning",
                        actionLabel = "View All",
                        onAction    = { navController.navigate(Screen.MyCourses.route) }
                    )
                    Spacer(Modifier.height(10.dp))
                    LazyRow(
                        contentPadding        = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.continueCourses) { course ->
                            CourseCard(
                                course  = course,
                                onClick = { navController.navigate(Screen.CourseDetail.createRoute(course.id)) }
                            )
                        }
                    }
                }
            }

            // ── Featured Courses ───────────────────────────────────────────
            item {
                Spacer(Modifier.height(20.dp))
                SectionHeader(
                    title       = "Featured Courses",
                    actionLabel = "See All",
                    onAction    = { navController.navigate(Screen.CourseList.createRoute()) }
                )
                Spacer(Modifier.height(10.dp))
                LazyRow(
                    contentPadding        = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.featuredCourses) { course ->
                        CourseCard(
                            course  = course,
                            onClick = { navController.navigate(Screen.CourseDetail.createRoute(course.id)) }
                        )
                    }
                }
            }

            // ── Live Now ───────────────────────────────────────────────────
            if (state.liveNow.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(20.dp))
                    SectionHeader(title = "🔴 Live Now")
                    Spacer(Modifier.height(10.dp))
                    LazyRow(
                        contentPadding        = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.liveNow) { live ->
                            LiveClassCard(live) {
                                navController.navigate(Screen.YoutubePlayer.createRoute(live.youtubeId))
                            }
                        }
                    }
                }
            }

            // ── Test Series Banner ─────────────────────────────────────────
            item {
                Spacer(Modifier.height(20.dp))
                TestSeriesBanner {
                    navController.navigate(Screen.TestSeriesList.route)
                }
            }

            // ── Current Affairs ────────────────────────────────────────────
            item {
                Spacer(Modifier.height(20.dp))
                SectionHeader(
                    title       = "Current Affairs",
                    actionLabel = "View All",
                    onAction    = { navController.navigate(Screen.CurrentAffairs.route) }
                )
                Spacer(Modifier.height(10.dp))
                Column(
                    modifier              = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement   = Arrangement.spacedBy(8.dp)
                ) {
                    state.latestAffairs.take(3).forEach { affair ->
                        CurrentAffairRow(affair) {
                            navController.navigate(Screen.CurrentAffairDetail.createRoute(affair.id))
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

// ─── Quick Action Item ────────────────────────────────────────────────────────
@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier            = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(Color.White)
            .padding(12.dp)
            .width(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier        = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFEEF0FF)),
            contentAlignment= Alignment.Center
        ) {
            Icon(icon, null, tint = Navy, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.height(5.dp))
        Text(
            text       = label,
            style      = MaterialTheme.typography.labelSmall,
            color      = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            maxLines   = 1
        )
    }
}

// ─── Live Class Card ──────────────────────────────────────────────────────────
@Composable
fun LiveClassCard(
    live: com.ramacademy.app.data.model.LiveClass,
    onClick: () -> Unit
) {
    Card(
        onClick   = onClick,
        modifier  = Modifier.width(200.dp),
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            coil.compose.AsyncImage(
                model             = live.thumbnail,
                contentDescription= live.title,
                contentScale      = androidx.compose.ui.layout.ContentScale.Crop,
                modifier          = Modifier.fillMaxWidth().height(110.dp)
            )
            // LIVE badge
            Surface(
                color  = Danger,
                shape  = RoundedCornerShape(6.dp),
                modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
            ) {
                Row(
                    modifier          = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Text("LIVE", style = MaterialTheme.typography.labelSmall, color = Color.White)
                }
            }
        }
        Column(modifier = Modifier.padding(10.dp)) {
            Text(live.title, style = MaterialTheme.typography.titleSmall, maxLines = 2)
            Text(live.teacherName, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
    }
}

// ─── Test Series Banner ──────────────────────────────────────────────────────
@Composable
fun TestSeriesBanner(onClick: () -> Unit) {
    Surface(
        color  = Color(0xFFFFF7ED),
        shape  = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Gold.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("✏️", fontSize = 32.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Test Series",
                    style      = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color      = NavyDark
                )
                Text(
                    "Practice with full-length mock tests",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
            Icon(Icons.Default.ArrowForwardIos, null, tint = Saff, modifier = Modifier.size(16.dp))
        }
    }
}

// ─── Current Affair Row ───────────────────────────────────────────────────────
@Composable
fun CurrentAffairRow(
    affair: com.ramacademy.app.data.model.CurrentAffair,
    onClick: () -> Unit
) {
    Surface(
        color  = Color.White,
        shape  = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier          = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(color = Navy.copy(alpha = 0.08f), shape = RoundedCornerShape(8.dp)) {
                Text(
                    affair.category.take(2).uppercase(),
                    style      = MaterialTheme.typography.labelSmall,
                    color      = Navy,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(affair.title, style = MaterialTheme.typography.titleSmall, maxLines = 2)
                Text(affair.date, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Icon(Icons.Default.ChevronRight, null, tint = TextMuted, modifier = Modifier.size(16.dp))
        }
    }
}
