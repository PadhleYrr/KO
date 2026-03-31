package com.ramacademy.app.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.ramacademy.app.data.model.Course
import com.ramacademy.app.data.model.TestSeries
import com.ramacademy.app.ui.theme.*

// ─── Top App Bar ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RamTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text       = title,
                style      = MaterialTheme.typography.headlineMedium,
                color      = Color.White,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        },
        actions = actions,
        colors  = TopAppBarDefaults.topAppBarColors(
            containerColor = Navy
        )
    )
}

// ─── Bottom Navigation ────────────────────────────────────────────────────────
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val route: String,
    val badgeCount: Int = 0
)

@Composable
fun RamBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home",    Icons.Default.Home,         Icons.Filled.Home,       "home"),
        BottomNavItem("Courses", Icons.Default.PlayCircle,   Icons.Filled.PlayCircle, "my_courses"),
        BottomNavItem("Tests",   Icons.Default.Quiz,         Icons.Filled.Quiz,       "test_hub"),
        BottomNavItem("Learn",   Icons.Default.MenuBook,     Icons.Filled.MenuBook,   "learn"),
        BottomNavItem("Profile", Icons.Default.AccountCircle,Icons.Filled.AccountCircle,"profile"),
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick  = { onNavigate(item.route) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount > 0)
                                Badge { Text(item.badgeCount.toString()) }
                        }
                    ) {
                        Icon(
                            if (selected) item.selectedIcon else item.icon,
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    Text(
                        text  = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = Navy,
                    selectedTextColor   = Navy,
                    indicatorColor      = Color(0xFFEEF0FF),
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}

// ─── Course Card ─────────────────────────────────────────────────────────────
@Composable
fun CourseCard(
    course: Course,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick   = onClick,
        modifier  = modifier.width(200.dp),
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                AsyncImage(
                    model             = course.thumbnail,
                    contentDescription= course.title,
                    contentScale      = ContentScale.Crop,
                    modifier          = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                )
                if (course.isFree) {
                    Surface(
                        color  = Saff,
                        shape  = RoundedCornerShape(bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            "FREE",
                            style    = MaterialTheme.typography.labelSmall,
                            color    = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                if (course.isPurchased) {
                    Surface(
                        color  = Success,
                        shape  = RoundedCornerShape(bottomStart = 8.dp),
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            "Enrolled",
                            style    = MaterialTheme.typography.labelSmall,
                            color    = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text     = course.title,
                    style    = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = course.instructor,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Gold, modifier = Modifier.size(12.dp))
                    Text(
                        text  = " ${course.rating}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Spacer(Modifier.weight(1f))
                    if (course.isPurchased) {
                        Text(
                            text  = "Continue →",
                            style = MaterialTheme.typography.labelSmall,
                            color = Navy,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (course.isFree) {
                        Text(
                            text  = "Free",
                            style = MaterialTheme.typography.labelSmall,
                            color = Success,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text  = "₹${course.discountPrice}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Navy,
                                fontWeight = FontWeight.Bold
                            )
                            if (course.price > course.discountPrice) {
                                Text(
                                    text  = "₹${course.price}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    ),
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
                if (course.isPurchased && course.progress > 0) {
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress       = { course.progress },
                        modifier       = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                        color          = Saff,
                        trackColor     = Color(0xFFE2E8F0)
                    )
                }
            }
        }
    }
}

// ─── Test Series Card ────────────────────────────────────────────────────────
@Composable
fun TestSeriesCard(
    test: TestSeries,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick   = onClick,
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier           = Modifier.padding(16.dp),
            verticalAlignment  = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon box
            Box(
                modifier        = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEF0FF)),
                contentAlignment= Alignment.Center
            ) {
                Icon(
                    Icons.Default.Quiz,
                    contentDescription = null,
                    tint    = Navy,
                    modifier= Modifier.size(26.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = test.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Chip("${test.totalQuestions} Qs", Navy)
                    Chip("${test.duration} min", Saff)
                }
            }
            if (test.isPaid && !test.isPurchased) {
                Surface(
                    color = Navy,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "₹${test.price}",
                        style    = MaterialTheme.typography.labelMedium,
                        color    = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            } else {
                Icon(Icons.Default.ArrowForwardIos, null, tint = TextMuted, modifier = Modifier.size(16.dp))
            }
        }
    }
}

// ─── Chip ────────────────────────────────────────────────────────────────────
@Composable
fun Chip(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text     = text,
            style    = MaterialTheme.typography.labelSmall,
            color    = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

// ─── Section Header ──────────────────────────────────────────────────────────
@Composable
fun SectionHeader(
    title: String,
    actionLabel: String = "See All",
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text  = title,
            style = MaterialTheme.typography.headlineSmall
        )
        if (onAction != null) {
            TextButton(onClick = onAction) {
                Text(
                    text  = actionLabel,
                    color = Saff,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

// ─── Streak Banner ───────────────────────────────────────────────────────────
@Composable
fun StreakBanner(streakDays: Int) {
    Surface(
        color  = Navy,
        shape  = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier              = Modifier.padding(16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🔥", fontSize = 28.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "$streakDays Day Streak!",
                    style      = MaterialTheme.typography.titleLarge,
                    color      = GoldLight,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Keep it up — study daily to maintain your streak",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.65f)
                )
            }
        }
    }
}

// ─── Stat Card ───────────────────────────────────────────────────────────────
@Composable
fun StatCard(
    label: String,
    value: String,
    accentColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            // accent top bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(accentColor)
            )
            Column(modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 18.dp, bottom = 14.dp)) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                Spacer(Modifier.height(8.dp))
                Text(value, style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp))
                Text(label, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }
    }
}

// ─── Empty State ─────────────────────────────────────────────────────────────
@Composable
fun EmptyState(
    icon: ImageVector = Icons.Default.SearchOff,
    title: String,
    subtitle: String = "",
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment   = Alignment.CenterHorizontally,
        verticalArrangement   = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint     = BorderLight
        )
        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, color = TextMuted)
        if (subtitle.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
        }
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onAction,
                colors  = ButtonDefaults.buttonColors(containerColor = Navy)
            ) {
                Text(actionLabel)
            }
        }
    }
}

// ─── Loading Shimmer ─────────────────────────────────────────────────────────
@Composable
fun ShimmerCard(modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(Color(0xFFE2E8F0))
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFE2E8F0))
            )
        }
    }
}

// ─── Navy Button ─────────────────────────────────────────────────────────────
@Composable
fun NavyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null
) {
    Button(
        onClick  = onClick,
        enabled  = enabled && !loading,
        modifier = modifier.height(50.dp),
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = Navy)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color    = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            if (icon != null) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(text, style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
    }
}

// ─── Saff Button ─────────────────────────────────────────────────────────────
@Composable
fun SaffButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        modifier = modifier.height(50.dp),
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = Saff)
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = Color.White)
    }
}
