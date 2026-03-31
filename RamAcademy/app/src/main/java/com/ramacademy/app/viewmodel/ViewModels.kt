package com.ramacademy.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramacademy.app.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── Auth ─────────────────────────────────────────────────────────────────────
data class AuthState(
    val isLoggedIn: Boolean = false,
    val loading:    Boolean = false,
    val error:      String? = null,
    val user:       User?   = null
)

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(phoneOrEmail: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(loading = true, error = null)
            try {
                // TODO: connect to Firebase Auth / backend API
                // val result = authRepository.login(phoneOrEmail, password)
                _authState.value = _authState.value.copy(loading = false, isLoggedIn = true)
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(loading = false, error = e.message)
            }
        }
    }

    fun register(name: String, phone: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(loading = true, error = null)
            try {
                // TODO: connect to Firebase Auth / backend API
                _authState.value = _authState.value.copy(loading = false, isLoggedIn = true)
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(loading = false, error = e.message)
            }
        }
    }

    fun verifyOtp(phone: String, otp: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(loading = true, error = null)
            // TODO: Firebase phone auth verify
            _authState.value = _authState.value.copy(loading = false, isLoggedIn = true)
        }
    }

    fun resendOtp(phone: String) {
        viewModelScope.launch {
            // TODO: resend OTP via Firebase
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(loading = true)
            // TODO: Firebase sendPasswordResetEmail
            _authState.value = _authState.value.copy(loading = false)
        }
    }

    fun logout() {
        _authState.value = AuthState()
    }
}

// ─── Home ─────────────────────────────────────────────────────────────────────
data class HomeState(
    val enrolledCount:  Int = 0,
    val testsDone:      Int = 0,
    val points:         Int = 0,
    val streak:         Int = 0,
    val featuredCourses:List<Course>      = emptyList(),
    val continueCourses:List<Course>      = emptyList(),
    val liveNow:        List<LiveClass>   = emptyList(),
    val latestAffairs:  List<CurrentAffair> = emptyList(),
    val loading:        Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(HomeState(loading = true))
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init { loadHome() }

    private fun loadHome() {
        viewModelScope.launch {
            // TODO: fetch from repository
            _state.value = HomeState(
                enrolledCount = 3,
                testsDone     = 12,
                points        = 840,
                streak        = 5,
                loading       = false
            )
        }
    }
}

// ─── Course ───────────────────────────────────────────────────────────────────
@HiltViewModel
class CourseViewModel @Inject constructor() : ViewModel() {
    private val _course   = MutableStateFlow<Course?>(null)
    private val _chapters = MutableStateFlow<List<Chapter>>(emptyList())
    private val _loading  = MutableStateFlow(true)

    val course:   StateFlow<Course?>        = _course.asStateFlow()
    val chapters: StateFlow<List<Chapter>>  = _chapters.asStateFlow()
    val loading:  StateFlow<Boolean>        = _loading.asStateFlow()

    fun loadCourse(courseId: String) {
        viewModelScope.launch {
            _loading.value = true
            // TODO: fetch from repository
            _loading.value = false
        }
    }

    fun enrollFree(courseId: String) {
        viewModelScope.launch {
            // TODO: enroll via API
        }
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            // TODO: toggle bookmark
        }
    }
}

// ─── Test ─────────────────────────────────────────────────────────────────────
data class TestState(
    val testTitle:       String            = "",
    val questions:       List<Question>    = emptyList(),
    val currentIndex:    Int               = 0,
    val answers:         Map<String, Int>  = emptyMap(),
    val flagged:         Set<String>       = emptySet(),
    val remainingSeconds:Int               = 3600,
    val isSubmitted:     Boolean           = false,
    val answeredCount:   Int               = 0
)

@HiltViewModel
class TestViewModel @Inject constructor() : ViewModel() {
    private val _testState = MutableStateFlow(TestState())
    val testState: StateFlow<TestState> = _testState.asStateFlow()

    private val _result = MutableStateFlow<TestResult?>(null)
    val result: StateFlow<TestResult?> = _result.asStateFlow()

    fun loadTest(testId: String) {
        viewModelScope.launch {
            // TODO: load questions from repository
        }
    }

    fun loadResult(testId: String) {
        viewModelScope.launch {
            // TODO: load result from repository
        }
    }

    fun selectAnswer(questionId: String, optionIndex: Int) {
        val updated = _testState.value.answers.toMutableMap()
        updated[questionId] = optionIndex
        _testState.value = _testState.value.copy(
            answers       = updated,
            answeredCount = updated.size
        )
    }

    fun toggleFlag(questionId: String) {
        val updated = _testState.value.flagged.toMutableSet()
        if (updated.contains(questionId)) updated.remove(questionId) else updated.add(questionId)
        _testState.value = _testState.value.copy(flagged = updated)
    }

    fun next() {
        val s = _testState.value
        if (s.currentIndex < s.questions.size - 1) {
            _testState.value = s.copy(currentIndex = s.currentIndex + 1)
        }
    }

    fun prev() {
        val s = _testState.value
        if (s.currentIndex > 0) {
            _testState.value = s.copy(currentIndex = s.currentIndex - 1)
        }
    }

    fun jumpTo(index: Int) {
        _testState.value = _testState.value.copy(currentIndex = index)
    }

    fun tickTimer() {
        val s = _testState.value
        if (s.remainingSeconds > 0) {
            _testState.value = s.copy(remainingSeconds = s.remainingSeconds - 1)
        }
    }

    fun submit() {
        val s = _testState.value
        _testState.value = s.copy(isSubmitted = true)
        viewModelScope.launch {
            // TODO: submit to backend, calculate result
        }
    }
}

// ─── Profile ──────────────────────────────────────────────────────────────────
@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init { loadProfile() }

    private fun loadProfile() {
        viewModelScope.launch {
            // TODO: load user from Firebase/repository
        }
    }

    fun updateProfile(name: String, phone: String) {
        viewModelScope.launch {
            // TODO: update profile
        }
    }

    fun updateAvatar(imageUri: String) {
        viewModelScope.launch {
            // TODO: upload avatar image
        }
    }
}
