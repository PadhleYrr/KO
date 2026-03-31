package com.ramacademy.app.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ramacademy.app.data.model.Chapter
import com.ramacademy.app.data.model.Lecture
import com.ramacademy.app.ui.Screen
import com.ramacademy.app.ui.components.*
import com.ramacademy.app.ui.theme.*
import com.ramacademy.app.viewmodel.CourseViewModel

@Composable
fun CourseDetailScreen(
    navController: NavController,
    courseId: String,
    vm: CourseViewModel = hiltViewModel()
) {
    val course   by vm.course.collectAsState()
    val chapters by vm.chapters.collectAsState()
    val loading  by vm.loading.collectAsState()
    var expandedChapter by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(courseId) { vm.loadCourse(courseId) }

    Scaffold(
        topBar = {
            RamTopBar(
                title  = course?.title ?: "Course",
                onBack = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { vm.toggleBookmark() }) {
                        Icon(Icons.Default.BookmarkBorder, null, tint = Color.White)
                    }
                    IconButton(onClick = { /* share */ }) {
                        Icon(Icons.Default.Share, null, tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            course?.let { c ->
                Surface(shadowElevation = 8.dp, color = Color.White) {
                    Row(
                        modifier              = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        if (c.isPurchased) {
                            NavyButton(
                                text     = "Continue Learning",
                                onClick  = { navController.navigate(Screen.MyCourses.route) },
                                icon     = Icons.Default.PlayArrow,
                                modifier = Modifier.weight(1f)
                            )
                        } else if (c.isFree) {
                            SaffButton(
                                text     = "Enroll Free",
                                onClick  = { vm.enrollFree(courseId) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("₹${c.discountPrice}", style = MaterialTheme.typography.headlineMedium, color = Navy, fontWeight = FontWeight.ExtraBold)
                                if (c.price > c.discountPrice) {
                                    Text("₹${c.price}", style = MaterialTheme.typography.bodySmall.copy(
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    ), color = TextMuted)
                                }
                            }
                            SaffButton("Buy Now", onClick = { navController.navigate(Screen.Checkout.createRoute(courseId)) }, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Navy)
            }
            return@Scaffold
        }

        course?.let { c ->
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) {

                // Thumbnail
                AsyncImage(
                    model             = c.thumbnail,
                    contentDescription= c.title,
                    contentScale      = ContentScale.Crop,
                    modifier          = Modifier.fillMaxWidth().height(220.dp)
                )

                // Course info
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(c.title, style = MaterialTheme.typography.headlineLarge)
                    Spacer(Modifier.height(4.dp))
                    Text(c.subtitle, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                    Spacer(Modifier.height(12.dp))

                    // Instructor
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Person, null, tint = Navy, modifier = Modifier.size(16.dp))
                        Text(c.instructor, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.height(12.dp))

                    // Meta chips row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Chip("⭐ ${c.rating}", Gold)
                        Chip("${c.totalVideos} Videos", Navy)
                        Chip(c.totalDuration, TextMuted)
                        Chip(c.language, Saff)
                    }

                    if (c.isPurchased && c.progress > 0) {
                        Spacer(Modifier.height(12.dp))
                        Text("Progress: ${(c.progress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                        Spacer(Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress   = { c.progress },
                            modifier   = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color      = Saff,
                            trackColor = BorderLight
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))

                    // Description
                    Text("About this Course", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text(c.description, style = MaterialTheme.typography.bodyMedium, color = TextMuted)

                    Spacer(Modifier.height(20.dp))
                    Text("Course Content", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(10.dp))
                }

                // Chapters accordion
                chapters.forEach { chapter ->
                    ChapterAccordion(
                        chapter        = chapter,
                        isExpanded     = expandedChapter == chapter.id,
                        isPurchased    = c.isPurchased,
                        onToggle       = {
                            expandedChapter = if (expandedChapter == chapter.id) null else chapter.id
                        },
                        onLectureClick = { lecture ->
                            if (!lecture.isLocked || c.isPurchased || lecture.isFree) {
                                navController.navigate(
                                    if (lecture.youtubeId.isNotEmpty())
                                        Screen.YoutubePlayer.createRoute(lecture.youtubeId)
                                    else
                                        Screen.VideoPlayer.createRoute(lecture.id)
                                )
                            }
                        }
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ChapterAccordion(
    chapter: Chapter,
    isExpanded: Boolean,
    isPurchased: Boolean,
    onToggle: () -> Unit,
    onLectureClick: (Lecture) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Chapter header
        Surface(
            color  = Color(0xFFF8F9FF),
            border = BorderStroke(1.dp, BorderLight),
            modifier = Modifier.fillMaxWidth().clickable(onClick = onToggle)
        ) {
            Row(
                modifier          = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "${chapter.order}",
                    style      = MaterialTheme.typography.labelLarge,
                    color      = Navy,
                    fontWeight = FontWeight.ExtraBold,
                    modifier   = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEEF0FF))
                        .wrapContentSize()
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(chapter.name, style = MaterialTheme.typography.titleMedium)
                    Text("${chapter.lectures.size} lectures", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
                Icon(
                    if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    null, tint = TextMuted
                )
            }
        }

        // Lectures
        if (isExpanded) {
            chapter.lectures.forEach { lecture ->
                LectureRow(
                    lecture     = lecture,
                    isPurchased = isPurchased,
                    onClick     = { onLectureClick(lecture) }
                )
            }
        }
    }
}

@Composable
fun LectureRow(lecture: Lecture, isPurchased: Boolean, onClick: () -> Unit) {
    val isAccessible = isPurchased || lecture.isFree
    Surface(
        color    = if (lecture.isCompleted) Color(0xFFF0FDF4) else Color.White,
        border   = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth().clickable(enabled = isAccessible, onClick = onClick)
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                when {
                    lecture.isCompleted -> Icons.Default.CheckCircle
                    isAccessible -> Icons.Default.PlayCircle
                    else -> Icons.Default.Lock
                },
                null,
                tint = when {
                    lecture.isCompleted -> Success
                    isAccessible -> Navy
                    else -> TextMuted
                },
                modifier = Modifier.size(22.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    lecture.title,
                    style  = MaterialTheme.typography.bodyMedium,
                    color  = if (isAccessible) TextPrimary else TextMuted
                )
                if (lecture.duration.isNotEmpty()) {
                    Text(lecture.duration, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
            }
            if (lecture.isFree && !isPurchased) {
                Chip("FREE", Success)
            }
        }
    }
}
