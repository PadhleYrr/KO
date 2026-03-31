package com.ramacademy.app.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.ramacademy.app.ui.screens.*

@Composable
fun RamAcademyNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = Screen.Splash.route,
        enterTransition  = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec  = tween(300)
            )
        },
        exitTransition   = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec  = tween(300)
            )
        },
        popExitTransition  = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            )
        }
    ) {
        // ── Splash & Auth ─────────────────────────────────────────────────────
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(
            route     = Screen.OtpVerify.route,
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) {
            OtpVerifyScreen(navController, it.arguments?.getString("phone") ?: "")
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController)
        }

        // ── Main (Bottom Nav) ─────────────────────────────────────────────────
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.MyCourses.route) {
            MyCoursesScreen(navController)
        }
        composable(Screen.TestHub.route) {
            TestHubScreen(navController)
        }
        composable(Screen.Learn.route) {
            LearnScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // ── Courses ───────────────────────────────────────────────────────────
        composable(
            route     = Screen.CourseList.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) {
            CourseListScreen(navController, it.arguments?.getString("category") ?: "all")
        }
        composable(
            route     = Screen.CourseDetail.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) {
            CourseDetailScreen(navController, it.arguments?.getString("courseId") ?: "")
        }
        composable(
            route     = Screen.VideoPlayer.route,
            arguments = listOf(navArgument("lectureId") { type = NavType.StringType })
        ) {
            VideoPlayerScreen(navController, it.arguments?.getString("lectureId") ?: "")
        }
        composable(
            route     = Screen.YoutubePlayer.route,
            arguments = listOf(navArgument("videoId") { type = NavType.StringType })
        ) {
            YoutubePlayerScreen(navController, it.arguments?.getString("videoId") ?: "")
        }

        // ── Test ──────────────────────────────────────────────────────────────
        composable(Screen.TestSeriesList.route) {
            TestSeriesListScreen(navController)
        }
        composable(
            route     = Screen.TestDetail.route,
            arguments = listOf(navArgument("testId") { type = NavType.StringType })
        ) {
            TestDetailScreen(navController, it.arguments?.getString("testId") ?: "")
        }
        composable(
            route     = Screen.TestAttempt.route,
            arguments = listOf(navArgument("testId") { type = NavType.StringType })
        ) {
            TestAttemptScreen(navController, it.arguments?.getString("testId") ?: "")
        }
        composable(
            route     = Screen.TestResult.route,
            arguments = listOf(navArgument("testId") { type = NavType.StringType })
        ) {
            TestResultScreen(navController, it.arguments?.getString("testId") ?: "")
        }
        composable(Screen.DailyQuiz.route)  { DailyQuizScreen(navController) }
        composable(Screen.BookmarkTest.route) { BookmarkTestScreen(navController) }

        // ── Study Material ────────────────────────────────────────────────────
        composable(
            route     = Screen.StudyMaterial.route,
            arguments = listOf(navArgument("subject") { type = NavType.StringType })
        ) {
            StudyMaterialScreen(navController, it.arguments?.getString("subject") ?: "all")
        }
        composable(
            route     = Screen.PdfViewer.route,
            arguments = listOf(navArgument("materialId") { type = NavType.StringType })
        ) {
            PdfViewerScreen(navController, it.arguments?.getString("materialId") ?: "")
        }

        // ── Current Affairs ───────────────────────────────────────────────────
        composable(Screen.CurrentAffairs.route) { CurrentAffairsScreen(navController) }
        composable(
            route     = Screen.CurrentAffairDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            CurrentAffairDetailScreen(navController, it.arguments?.getString("id") ?: "")
        }

        // ── Live ──────────────────────────────────────────────────────────────
        composable(Screen.LiveClasses.route)     { LiveClassesScreen(navController) }
        composable(Screen.RecordedClasses.route) { RecordedClassesScreen(navController) }

        // ── Teachers ──────────────────────────────────────────────────────────
        composable(Screen.Teachers.route) { TeachersScreen(navController) }
        composable(
            route     = Screen.TeacherDetail.route,
            arguments = listOf(navArgument("teacherId") { type = NavType.StringType })
        ) {
            TeacherDetailScreen(navController, it.arguments?.getString("teacherId") ?: "")
        }

        // ── Doubts ────────────────────────────────────────────────────────────
        composable(Screen.Doubts.route)    { DoubtsScreen(navController) }
        composable(Screen.AddDoubt.route)  { AddDoubtScreen(navController) }
        composable(
            route     = Screen.DoubtDetail.route,
            arguments = listOf(navArgument("doubtId") { type = NavType.StringType })
        ) {
            DoubtDetailScreen(navController, it.arguments?.getString("doubtId") ?: "")
        }

        // ── Misc ──────────────────────────────────────────────────────────────
        composable(Screen.Downloads.route)    { DownloadsScreen(navController) }
        composable(Screen.Notifications.route){ NotificationsScreen(navController) }
        composable(Screen.Settings.route)     { SettingsScreen(navController) }
        composable(Screen.ChangePassword.route){ ChangePasswordScreen(navController) }
        composable(Screen.Syllabus.route)     { SyllabusScreen(navController) }
        composable(Screen.TimeTable.route)    { TimeTableScreen(navController) }
        composable(Screen.Blog.route)         { BlogScreen(navController) }
        composable(Screen.About.route)        { AboutScreen(navController) }
        composable(Screen.Referral.route)     { ReferralScreen(navController) }
        composable(Screen.Store.route)        { StoreScreen(navController) }
        composable(Screen.Cart.route)         { CartScreen(navController) }
        composable(
            route     = Screen.Checkout.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) {
            CheckoutScreen(navController, it.arguments?.getString("courseId") ?: "")
        }
    }
}
