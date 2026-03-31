package com.ramacademy.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.navigation.NavController
import com.ramacademy.app.ui.Screen
import com.ramacademy.app.ui.components.*
import com.ramacademy.app.ui.theme.*

// ─── My Courses ───────────────────────────────────────────────────────────────
@Composable
fun MyCoursesScreen(navController: NavController) {
    var tab by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = { RamTopBar("My Courses") },
        bottomBar = {
            RamBottomNav(Screen.MyCourses.route) { navController.navigate(it) { launchSingleTop = true } }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = tab, containerColor = Color.White, contentColor = Navy) {
                Tab(selected = tab == 0, onClick = { tab = 0 }) {
                    Text("Enrolled", modifier = Modifier.padding(vertical = 14.dp))
                }
                Tab(selected = tab == 1, onClick = { tab = 1 }) {
                    Text("Completed", modifier = Modifier.padding(vertical = 14.dp))
                }
            }
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyState(
                    icon       = Icons.Default.PlayCircle,
                    title      = if (tab == 0) "No enrolled courses yet" else "No completed courses",
                    subtitle   = "Explore our courses and start learning",
                    actionLabel= "Browse Courses",
                    onAction   = { navController.navigate(Screen.CourseList.createRoute()) }
                )
            }
        }
    }
}

// ─── Test Hub ─────────────────────────────────────────────────────────────────
@Composable
fun TestHubScreen(navController: NavController) {
    Scaffold(
        topBar = { RamTopBar("Test Hub") },
        bottomBar = { RamBottomNav(Screen.TestHub.route) { navController.navigate(it) { launchSingleTop = true } } }
    ) { padding ->
        LazyColumn(
            modifier        = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("Practice & Exams", style = MaterialTheme.typography.headlineLarge) }
            item {
                TestHubCard("📅 Daily Quiz", "10 questions every day", "Streak Builder", Navy) {
                    navController.navigate(Screen.DailyQuiz.route)
                }
            }
            item {
                TestHubCard("⏱️ Timed Mock Test", "Full exam simulation", "Exam Mode", Saff) {
                    navController.navigate(Screen.TestSeriesList.route)
                }
            }
            item {
                TestHubCard("📄 Previous Year Papers", "PYQ from 2020–2024", "PYQ Bank", Gold) {
                    navController.navigate(Screen.TestSeriesList.route)
                }
            }
            item {
                TestHubCard("🔖 Bookmarked Questions", "Revisit saved questions", "My Bookmarks", Success) {
                    navController.navigate(Screen.BookmarkTest.route)
                }
            }
            item {
                TestHubCard("📊 Full Test Series", "Chapter-wise & subject-wise", "Test Series", Color(0xFF7C3AED)) {
                    navController.navigate(Screen.TestSeriesList.route)
                }
            }
        }
    }
}

@Composable
fun TestHubCard(title: String, subtitle: String, tag: String, color: Color, onClick: () -> Unit) {
    Card(
        onClick   = onClick,
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors    = CardDefaults.cardColors(Color.White),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier        = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(color.copy(alpha = 0.12f)),
                contentAlignment= Alignment.Center
            ) {
                Text(title.take(2), fontSize = 22.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title.drop(3), style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Surface(color = color, shape = RoundedCornerShape(8.dp)) {
                Text(tag, style = MaterialTheme.typography.labelSmall, color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
            }
        }
    }
}

// ─── Learn (Study Material) ───────────────────────────────────────────────────
@Composable
fun LearnScreen(navController: NavController) {
    val subjects = listOf("All","History","Geography","Polity","Economics","Science","Environment","Current Affairs","Maths","Reasoning")
    var selectedSubject by remember { mutableStateOf("All") }

    Scaffold(
        topBar = { RamTopBar("Study Material") },
        bottomBar = { RamBottomNav(Screen.Learn.route) { navController.navigate(it) { launchSingleTop = true } } }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyRow(
                contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subjects) { subject ->
                    FilterChip(
                        selected = selectedSubject == subject,
                        onClick  = {
                            selectedSubject = subject
                            navController.navigate(Screen.StudyMaterial.createRoute(subject))
                        },
                        label    = { Text(subject) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Navy,
                            selectedLabelColor     = Color.White
                        )
                    )
                }
            }
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyState(
                    icon     = Icons.Default.MenuBook,
                    title    = "Study material loading…",
                    subtitle = "Connect to backend to see PDFs & notes"
                )
            }
        }
    }
}

// ─── Profile ──────────────────────────────────────────────────────────────────
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        bottomBar = { RamBottomNav(Screen.Profile.route) { navController.navigate(it) { launchSingleTop = true } } }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Header
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Navy, NavyLight)))
                ) {
                    Column(
                        modifier            = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                        Box(
                            modifier        = Modifier.size(80.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment= Alignment.Center
                        ) {
                            Text("👤", fontSize = 36.sp)
                        }
                        Spacer(Modifier.height(10.dp))
                        Text("Student Name", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                        Text("student@email.com", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.65f))
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            ProfileStat("3", "Courses")
                            ProfileStat("12", "Tests")
                            ProfileStat("840", "Points")
                            ProfileStat("5🔥", "Streak")
                        }
                    }
                }
            }

            // Menu
            item { Spacer(Modifier.height(12.dp)) }
            item { ProfileMenuSection("Learning") }
            items(listOf(
                Triple(Icons.Default.PlayCircle, "My Courses",     Screen.MyCourses.route),
                Triple(Icons.Default.Download,   "Downloads",      Screen.Downloads.route),
                Triple(Icons.Default.BookmarkBorder, "Bookmarks",  Screen.BookmarkTest.route),
                Triple(Icons.Default.Schedule,   "Time Table",     Screen.TimeTable.route),
            )) { (icon, label, route) ->
                ProfileMenuItem(icon, label) { navController.navigate(route) }
            }
            item { ProfileMenuSection("Community") }
            items(listOf(
                Triple(Icons.Default.HelpOutline,"My Doubts",      Screen.Doubts.route),
                Triple(Icons.Default.People,     "Faculty",        Screen.Teachers.route),
                Triple(Icons.Default.Article,    "Blog",           Screen.Blog.route),
            )) { (icon, label, route) ->
                ProfileMenuItem(icon, label) { navController.navigate(route) }
            }
            item { ProfileMenuSection("Account") }
            items(listOf(
                Triple(Icons.Default.Person,        "Edit Profile",    "edit_profile"),
                Triple(Icons.Default.Lock,          "Change Password", Screen.ChangePassword.route),
                Triple(Icons.Default.CardGiftcard,  "Referral",        Screen.Referral.route),
                Triple(Icons.Default.Settings,      "Settings",        Screen.Settings.route),
                Triple(Icons.Default.Info,          "About",           Screen.About.route),
            )) { (icon, label, route) ->
                ProfileMenuItem(icon, label) { navController.navigate(route) }
            }
            item {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick  = { navController.navigate(Screen.Login.route) { popUpTo(0) } },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(50.dp),
                    shape    = RoundedCornerShape(12.dp),
                    border   = BorderStroke(1.5.dp, Danger),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = Danger)
                ) {
                    Icon(Icons.Default.Logout, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Logout")
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall, color = GoldLight, fontWeight = FontWeight.ExtraBold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.65f))
    }
}

@Composable
fun ProfileMenuSection(title: String) {
    Text(
        title,
        style    = MaterialTheme.typography.labelMedium,
        color    = TextMuted,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun ProfileMenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Surface(
        color    = Color.White,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(icon, null, tint = Navy, modifier = Modifier.size(20.dp))
            Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = BorderLight, modifier = Modifier.size(16.dp))
        }
        HorizontalDivider(modifier = Modifier.padding(start = 50.dp), color = BorderLight)
    }
}

// ─── Current Affairs ──────────────────────────────────────────────────────────
@Composable
fun CurrentAffairsScreen(navController: NavController) {
    Scaffold(
        topBar = { RamTopBar("Current Affairs", onBack = { navController.popBackStack() }) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            EmptyState(icon = Icons.Default.Newspaper, title = "Current Affairs", subtitle = "Connect backend to load articles")
        }
    }
}

@Composable
fun CurrentAffairDetailScreen(navController: NavController, id: String) {
    Scaffold(topBar = { RamTopBar("Article", onBack = { navController.popBackStack() }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Article #$id content here", color = TextMuted)
        }
    }
}

// ─── Downloads ────────────────────────────────────────────────────────────────
@Composable
fun DownloadsScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Downloads", onBack = { navController.popBackStack() }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            EmptyState(icon = Icons.Default.Download, title = "No downloads yet", subtitle = "Download lectures and PDFs for offline access")
        }
    }
}

// ─── Settings ─────────────────────────────────────────────────────────────────
@Composable
fun SettingsScreen(navController: NavController) {
    var notifications by remember { mutableStateOf(true) }
    var darkMode      by remember { mutableStateOf(false) }
    var autoDownload  by remember { mutableStateOf(false) }

    Scaffold(topBar = { RamTopBar("Settings", onBack = { navController.popBackStack() }) }) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item { ProfileMenuSection("Preferences") }
            item {
                SettingsToggle("Push Notifications", Icons.Default.Notifications, notifications) { notifications = it }
            }
            item {
                SettingsToggle("Dark Mode", Icons.Default.DarkMode, darkMode) { darkMode = it }
            }
            item {
                SettingsToggle("Auto Download on WiFi", Icons.Default.WifiTethering, autoDownload) { autoDownload = it }
            }
            item { ProfileMenuSection("About") }
            item { ProfileMenuItem(Icons.Default.Info, "App Version 2.0.0") {} }
            item { ProfileMenuItem(Icons.Default.PrivacyTip, "Privacy Policy") {} }
            item { ProfileMenuItem(Icons.Default.Gavel, "Terms of Service") {} }
        }
    }
}

@Composable
fun SettingsToggle(label: String, icon: ImageVector, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Surface(color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier          = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(icon, null, tint = Navy, modifier = Modifier.size(20.dp))
            Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Switch(checked = checked, onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Navy))
        }
        HorizontalDivider(modifier = Modifier.padding(start = 50.dp), color = BorderLight)
    }
}

// ─── Stub Screens (filled later) ─────────────────────────────────────────────
@Composable fun CourseListScreen(navController: NavController, category: String) {
    Scaffold(topBar = { RamTopBar("Courses", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.PlayCircle, "Courses ($category)", "Connect to API to load courses")
        }
    }
}
@Composable fun VideoPlayerScreen(navController: NavController, lectureId: String) {
    Scaffold(topBar = { RamTopBar("Video Player", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            Text("ExoPlayer for lecture: $lectureId", color = TextMuted)
        }
    }
}
@Composable fun YoutubePlayerScreen(navController: NavController, videoId: String) {
    Scaffold(topBar = { RamTopBar("YouTube", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            Text("YouTube embed: $videoId", color = TextMuted)
        }
    }
}
@Composable fun TestSeriesListScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Test Series", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.Quiz, "Test Series", "Connect to API to load tests")
        }
    }
}
@Composable fun TestDetailScreen(navController: NavController, testId: String) {
    Scaffold(topBar = { RamTopBar("Test Details", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            NavyButton("Start Test", onClick = { navController.navigate(Screen.TestAttempt.createRoute(testId)) })
        }
    }
}
@Composable fun DailyQuizScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Daily Quiz", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.Today, "Today's 10 Questions", "Connect to API")
        }
    }
}
@Composable fun BookmarkTestScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Bookmarked Questions", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.Bookmark, "No bookmarks yet", "Flag questions to save them here")
        }
    }
}
@Composable fun StudyMaterialScreen(navController: NavController, subject: String) {
    Scaffold(topBar = { RamTopBar("Study Material", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.PictureAsPdf, "Material for $subject", "Connect to API")
        }
    }
}
@Composable fun PdfViewerScreen(navController: NavController, materialId: String) {
    Scaffold(topBar = { RamTopBar("PDF Viewer", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            Text("Render PDF: $materialId", color = TextMuted)
        }
    }
}
@Composable fun LiveClassesScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Live Classes", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.LiveTv, "No live classes right now", "Check the timetable")
        }
    }
}
@Composable fun RecordedClassesScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Recorded Classes", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.VideoLibrary, "Recorded Classes", "Connect to API")
        }
    }
}
@Composable fun TeachersScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Our Faculty", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.People, "Faculty", "Connect to API")
        }
    }
}
@Composable fun TeacherDetailScreen(navController: NavController, teacherId: String) {
    Scaffold(topBar = { RamTopBar("Faculty Profile", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            Text("Teacher: $teacherId", color = TextMuted)
        }
    }
}
@Composable fun DoubtsScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("My Doubts", onBack = { navController.popBackStack() }) },
        floatingActionButton = { FloatingActionButton(onClick = { navController.navigate(Screen.AddDoubt.route) }, containerColor = Navy) { Icon(Icons.Default.Add, null, tint = Color.White) } }
    ) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.HelpOutline, "No doubts posted", "Tap + to ask your first doubt", "Ask Doubt") { navController.navigate(Screen.AddDoubt.route) }
        }
    }
}
@Composable fun AddDoubtScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Ask a Doubt", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            Text("Doubt form goes here", color = TextMuted)
        }
    }
}
@Composable fun DoubtDetailScreen(navController: NavController, doubtId: String) {
    Scaffold(topBar = { RamTopBar("Doubt", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            Text("Doubt #$doubtId", color = TextMuted)
        }
    }
}
@Composable fun NotificationsScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Notifications", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.Notifications, "No notifications", "")
        }
    }
}
@Composable fun ChangePasswordScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Change Password", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            Text("Change password form", color = TextMuted)
        }
    }
}
@Composable fun SyllabusScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Syllabus", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.ListAlt, "Syllabus", "Connect to API")
        }
    }
}
@Composable fun TimeTableScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Time Table", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.CalendarMonth, "Time Table", "Connect to API")
        }
    }
}
@Composable fun BlogScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Blog", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.Article, "Blog posts", "Connect to API")
        }
    }
}
@Composable fun AboutScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("About Ram Academy", onBack = { navController.popBackStack() }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(40.dp))
            Text("🎓", fontSize = 72.sp)
            Spacer(Modifier.height(16.dp))
            Text("Ram Academy", style = MaterialTheme.typography.displayMedium)
            Text("Version 2.0.0", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            Spacer(Modifier.height(24.dp))
            Text("Prepare. Perform. Prevail.", style = MaterialTheme.typography.headlineSmall, color = Navy)
            Spacer(Modifier.height(16.dp))
            Text("Ram Academy is your complete exam preparation companion — with video courses, test series, study material, live classes, and doubt resolution all in one place.",
                style = MaterialTheme.typography.bodyMedium, color = TextMuted)
        }
    }
}
@Composable fun ReferralScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Referral", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            Text("Referral screen", color = TextMuted)
        }
    }
}
@Composable fun StoreScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Store", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.Store, "Store", "Connect to API")
        }
    }
}
@Composable fun CartScreen(navController: NavController) {
    Scaffold(topBar = { RamTopBar("Cart", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Default.ShoppingCart, "Your cart is empty", "Browse courses to add", "Browse", { navController.navigate(Screen.CourseList.createRoute()) })
        }
    }
}
@Composable fun CheckoutScreen(navController: NavController, courseId: String) {
    Scaffold(topBar = { RamTopBar("Checkout", onBack = { navController.popBackStack() }) }) { p ->
        Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
            Text("Payment screen for $courseId", color = TextMuted)
        }
    }
}
