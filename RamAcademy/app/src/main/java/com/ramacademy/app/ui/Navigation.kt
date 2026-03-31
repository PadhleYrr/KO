package com.ramacademy.app.ui

sealed class Screen(val route: String) {

    // Auth
    object Splash        : Screen("splash")
    object Login         : Screen("login")
    object Register      : Screen("register")
    object OtpVerify     : Screen("otp_verify/{phone}") {
        fun createRoute(phone: String) = "otp_verify/$phone"
    }
    object ForgotPassword: Screen("forgot_password")

    // Main bottom-nav
    object Home          : Screen("home")
    object MyCourses     : Screen("my_courses")
    object TestHub       : Screen("test_hub")
    object Learn         : Screen("learn")
    object Profile       : Screen("profile")

    // Courses
    object CourseList    : Screen("course_list/{category}") {
        fun createRoute(category: String = "all") = "course_list/$category"
    }
    object CourseDetail  : Screen("course_detail/{courseId}") {
        fun createRoute(id: String) = "course_detail/$id"
    }
    object VideoPlayer   : Screen("video_player/{lectureId}") {
        fun createRoute(id: String) = "video_player/$id"
    }
    object YoutubePlayer : Screen("youtube_player/{videoId}") {
        fun createRoute(id: String) = "youtube_player/$id"
    }

    // Test
    object TestSeriesList: Screen("test_series_list")
    object TestDetail    : Screen("test_detail/{testId}") {
        fun createRoute(id: String) = "test_detail/$id"
    }
    object TestAttempt   : Screen("test_attempt/{testId}") {
        fun createRoute(id: String) = "test_attempt/$id"
    }
    object TestResult    : Screen("test_result/{testId}") {
        fun createRoute(id: String) = "test_result/$id"
    }
    object DailyQuiz     : Screen("daily_quiz")
    object BookmarkTest  : Screen("bookmark_test")

    // Study Material
    object StudyMaterial : Screen("study_material/{subject}") {
        fun createRoute(subject: String = "all") = "study_material/$subject"
    }
    object PdfViewer     : Screen("pdf_viewer/{materialId}") {
        fun createRoute(id: String) = "pdf_viewer/$id"
    }

    // Current Affairs
    object CurrentAffairs: Screen("current_affairs")
    object CurrentAffairDetail: Screen("current_affair_detail/{id}") {
        fun createRoute(id: String) = "current_affair_detail/$id"
    }

    // Live & Recorded
    object LiveClasses   : Screen("live_classes")
    object RecordedClasses:Screen("recorded_classes")

    // Teachers
    object Teachers      : Screen("teachers")
    object TeacherDetail : Screen("teacher_detail/{teacherId}") {
        fun createRoute(id: String) = "teacher_detail/$id"
    }

    // Doubts
    object Doubts        : Screen("doubts")
    object AddDoubt      : Screen("add_doubt")
    object DoubtDetail   : Screen("doubt_detail/{doubtId}") {
        fun createRoute(id: String) = "doubt_detail/$id"
    }

    // Downloads
    object Downloads     : Screen("downloads")

    // Notifications
    object Notifications : Screen("notifications")

    // Settings
    object Settings      : Screen("settings")
    object ChangePassword: Screen("change_password")

    // Store / Cart
    object Store         : Screen("store")
    object Cart          : Screen("cart")
    object Checkout      : Screen("checkout/{courseId}") {
        fun createRoute(id: String) = "checkout/$id"
    }

    // Miscellaneous
    object Syllabus      : Screen("syllabus")
    object TimeTable     : Screen("time_table")
    object Blog          : Screen("blog")
    object About         : Screen("about")
    object Referral      : Screen("referral")
}
