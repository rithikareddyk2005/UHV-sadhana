package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.components.RoleSwitcherDialog
import com.example.ui.components.UhvBottomNavigationBar
import com.example.ui.components.UhvTopAppBar
import com.example.ui.screens.*
import com.example.ui.theme.UhvSadhanaTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UhvViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UhvSadhanaTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(viewModel: UhvViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val language by viewModel.language.collectAsState()
    val isDemoMode by viewModel.isDemoMode.collectAsState()

    val modules by viewModel.allModules.collectAsState()
    val students by viewModel.allStudents.collectAsState()
    val certificates by viewModel.allCertificates.collectAsState()
    val studentCertificates by viewModel.studentCertificates.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val events by viewModel.events.collectAsState()
    val auditLogs by viewModel.auditLogs.collectAsState()
    val leaderboard by viewModel.leaderboard.collectAsState()

    val selectedModule by viewModel.selectedModule.collectAsState()
    val selectedLesson by viewModel.selectedLesson.collectAsState()
    val selectedActivity by viewModel.selectedActivity.collectAsState()
    val quizQuestions by viewModel.currentQuizQuestions.collectAsState()
    val userQuizAnswers by viewModel.userQuizAnswers.collectAsState()

    var showRoleSwitcher by remember { mutableStateOf(false) }

    val userRole = currentUser?.role ?: UserRole.STUDENT

    Scaffold(
        topBar = {
            UhvTopAppBar(
                viewModel = viewModel,
                currentUser = currentUser,
                language = language,
                isDemoMode = isDemoMode,
                onRoleSwitchClick = { showRoleSwitcher = true }
            )
        },
        bottomBar = {
            UhvBottomNavigationBar(
                currentScreen = currentScreen,
                userRole = userRole,
                onNavigate = { screen -> viewModel.navigateTo(screen) }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.STUDENT_DASHBOARD -> {
                    StudentDashboardScreen(
                        viewModel = viewModel,
                        currentUser = currentUser,
                        language = language,
                        modules = modules,
                        announcements = announcements,
                        events = events
                    )
                }

                Screen.MODULE_DETAIL -> {
                    selectedModule?.let { mod ->
                        ModuleDetailScreen(
                            viewModel = viewModel,
                            module = mod,
                            language = language
                        )
                    }
                }

                Screen.LESSON_DETAIL -> {
                    selectedLesson?.let { les ->
                        LessonDetailScreen(
                            viewModel = viewModel,
                            lesson = les,
                            language = language
                        )
                    }
                }

                Screen.PRACTICE_ACTIVITY -> {
                    selectedActivity?.let { act ->
                        PracticeActivityScreen(
                            viewModel = viewModel,
                            activity = act,
                            language = language
                        )
                    }
                }

                Screen.QUIZ -> {
                    selectedModule?.let { mod ->
                        QuizScreen(
                            viewModel = viewModel,
                            module = mod,
                            questions = quizQuestions,
                            userAnswers = userQuizAnswers,
                            language = language
                        )
                    }
                }

                Screen.CERTIFICATES -> {
                    CertificatesScreen(
                        viewModel = viewModel,
                        certificates = studentCertificates,
                        language = language
                    )
                }

                Screen.PUBLIC_VERIFY -> {
                    PublicVerifyScreen(
                        viewModel = viewModel,
                        language = language
                    )
                }

                Screen.AI_MENTOR -> {
                    AiMentorScreen(
                        viewModel = viewModel,
                        language = language
                    )
                }

                Screen.LEADERBOARD -> {
                    LeaderboardScreen(
                        viewModel = viewModel,
                        leaderboard = leaderboard,
                        language = language
                    )
                }

                Screen.DAILY_REFLECTIONS -> {
                    val reflectionViewModel: com.example.ui.viewmodel.DailyReflectionViewModel = viewModel()
                    DailyReflectionsScreen(
                        uhvViewModel = viewModel,
                        reflectionViewModel = reflectionViewModel,
                        currentUser = currentUser,
                        language = language
                    )
                }

                Screen.ADMIN_DASHBOARD -> {
                    AdminDashboardScreen(
                        viewModel = viewModel,
                        students = students,
                        certificates = certificates,
                        auditLogs = auditLogs,
                        language = language
                    )
                }

                Screen.ADMIN_STUDENT_IMPORT -> {
                    AdminStudentImportScreen(
                        viewModel = viewModel,
                        language = language
                    )
                }

                Screen.ADMIN_STUDENTS_DIRECTORY -> {
                    AdminStudentsDirectoryScreen(
                        viewModel = viewModel,
                        students = students,
                        language = language
                    )
                }

                Screen.ADMIN_AI_REVIEW -> {
                    AdminAiReviewScreen(
                        viewModel = viewModel,
                        language = language
                    )
                }

                Screen.ADMIN_AUDIT_LOGS -> {
                    AdminAuditLogsScreen(
                        viewModel = viewModel,
                        auditLogs = auditLogs,
                        language = language
                    )
                }

                Screen.FACULTY_REVIEW -> {
                    FacultyReviewScreen(
                        viewModel = viewModel,
                        language = language
                    )
                }

                Screen.EVENTS_LIST -> {
                    StudentDashboardScreen(
                        viewModel = viewModel,
                        currentUser = currentUser,
                        language = language,
                        modules = modules,
                        announcements = announcements,
                        events = events
                    )
                }
            }
        }
    }

    if (showRoleSwitcher) {
        RoleSwitcherDialog(
            currentRole = userRole,
            onRoleSelected = { newRole ->
                viewModel.switchUserRole(newRole)
            },
            onDismiss = { showRoleSwitcher = false }
        )
    }
}
