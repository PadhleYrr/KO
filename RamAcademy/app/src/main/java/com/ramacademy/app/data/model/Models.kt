package com.ramacademy.app.data.model

import com.google.gson.annotations.SerializedName

// ─── Auth ────────────────────────────────────────────────────────────────────
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: String = "",
    val enrolledCourses: List<String> = emptyList(),
    val isPremium: Boolean = false,
    val streakDays: Int = 0,
    val totalPoints: Int = 0
)

// ─── Course ──────────────────────────────────────────────────────────────────
data class Course(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val thumbnail: String = "",
    val instructor: String = "",
    val instructorAvatar: String = "",
    val rating: Float = 0f,
    val totalRatings: Int = 0,
    val studentsEnrolled: Int = 0,
    val totalVideos: Int = 0,
    val totalDuration: String = "",
    val price: Int = 0,
    val discountPrice: Int = 0,
    val category: String = "",
    val tags: List<String> = emptyList(),
    val isFree: Boolean = false,
    val isPurchased: Boolean = false,
    val progress: Float = 0f,
    val description: String = "",
    val language: String = "Hindi",
    val lastUpdated: String = ""
)

// ─── Video / Lecture ─────────────────────────────────────────────────────────
data class Lecture(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val youtubeId: String = "",
    val duration: String = "",
    val isLocked: Boolean = true,
    val isFree: Boolean = false,
    val isCompleted: Boolean = false,
    val thumbUrl: String = "",
    val order: Int = 0,
    val chapterId: String = "",
    val chapterName: String = ""
)

data class Chapter(
    val id: String = "",
    val name: String = "",
    val lectures: List<Lecture> = emptyList(),
    val order: Int = 0
)

// ─── Test / Quiz ─────────────────────────────────────────────────────────────
data class Question(
    val id: String = "",
    val text: String = "",
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0,
    val explanation: String = "",
    val chapter: String = "",
    val subject: String = "",
    val difficulty: String = "Medium",  // Easy / Medium / Hard
    val year: Int? = null,
    val isBookmarked: Boolean = false
)

data class TestSeries(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val totalQuestions: Int = 0,
    val duration: Int = 60,   // minutes
    val maxAttempts: Int = 0,
    val category: String = "",
    val isPaid: Boolean = false,
    val price: Int = 0,
    val isPurchased: Boolean = false,
    val schedule: String = ""
)

data class TestResult(
    val testId: String = "",
    val totalQuestions: Int = 0,
    val attempted: Int = 0,
    val correct: Int = 0,
    val wrong: Int = 0,
    val skipped: Int = 0,
    val score: Float = 0f,
    val timeTaken: Int = 0,
    val rank: Int = 0,
    val percentile: Float = 0f,
    val answers: Map<String, Int> = emptyMap()   // questionId → selectedIndex
)

// ─── Study Material ──────────────────────────────────────────────────────────
data class StudyMaterial(
    val id: String = "",
    val title: String = "",
    val subject: String = "",
    val type: MaterialType = MaterialType.PDF,
    val fileUrl: String = "",
    val thumbnailUrl: String = "",
    val sizeKb: Long = 0,
    val isPaid: Boolean = false,
    val isPurchased: Boolean = false,
    val downloadCount: Int = 0
)

enum class MaterialType { PDF, EBOOK, NOTES, ASSIGNMENT }

// ─── Current Affairs ─────────────────────────────────────────────────────────
data class CurrentAffair(
    val id: String = "",
    val title: String = "",
    val summary: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val date: String = "",
    val isBookmarked: Boolean = false
)

// ─── Teacher / Faculty ───────────────────────────────────────────────────────
data class Teacher(
    val id: String = "",
    val name: String = "",
    val subject: String = "",
    val qualification: String = "",
    val experience: String = "",
    val avatar: String = "",
    val rating: Float = 0f,
    val totalStudents: Int = 0,
    val bio: String = ""
)

// ─── Doubt ───────────────────────────────────────────────────────────────────
data class Doubt(
    val id: String = "",
    val question: String = "",
    val imageUrl: String = "",
    val subject: String = "",
    val userId: String = "",
    val userName: String = "",
    val answer: String = "",
    val isResolved: Boolean = false,
    val timestamp: Long = 0L,
    val comments: List<DoubtComment> = emptyList()
)

data class DoubtComment(
    val id: String = "",
    val text: String = "",
    val userId: String = "",
    val userName: String = "",
    val timestamp: Long = 0L
)

// ─── Live Class ──────────────────────────────────────────────────────────────
data class LiveClass(
    val id: String = "",
    val title: String = "",
    val subject: String = "",
    val teacherName: String = "",
    val teacherAvatar: String = "",
    val startTime: Long = 0L,
    val duration: Int = 60,
    val streamUrl: String = "",
    val youtubeId: String = "",
    val isLive: Boolean = false,
    val viewerCount: Int = 0,
    val thumbnail: String = ""
)

// ─── Notification ────────────────────────────────────────────────────────────
data class AppNotification(
    val id: String = "",
    val title: String = "",
    val body: String = "",
    val imageUrl: String = "",
    val type: String = "",
    val deepLink: String = "",
    val timestamp: Long = 0L,
    val isRead: Boolean = false
)

// ─── API Response Wrapper ────────────────────────────────────────────────────
data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data")    val data: T? = null,
    @SerializedName("error")   val error: String? = null
)

data class PaginatedResponse<T>(
    val items: List<T> = emptyList(),
    val total: Int = 0,
    val page: Int = 1,
    val pageSize: Int = 20,
    val hasMore: Boolean = false
)
