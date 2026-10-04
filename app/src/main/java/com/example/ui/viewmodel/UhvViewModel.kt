package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.InitialDataSeed
import com.example.data.model.*
import com.example.data.repository.UhvRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class Screen {
    STUDENT_DASHBOARD,
    MODULE_DETAIL,
    LESSON_DETAIL,
    PRACTICE_ACTIVITY,
    QUIZ,
    CERTIFICATES,
    PUBLIC_VERIFY,
    AI_MENTOR,
    LEADERBOARD,
    DAILY_REFLECTIONS,
    ADMIN_DASHBOARD,
    ADMIN_STUDENT_IMPORT,
    ADMIN_STUDENTS_DIRECTORY,
    ADMIN_AI_REVIEW,
    ADMIN_AUDIT_LOGS,
    FACULTY_REVIEW,
    EVENTS_LIST
}

enum class Language {
    EN, TE
}

class UhvViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UhvRepository = UhvRepository(AppDatabase.getDatabase(application))

    // Navigation BackStack
    private val _screenStack = MutableStateFlow(listOf(Screen.STUDENT_DASHBOARD))
    val currentScreen: StateFlow<Screen> = _screenStack
        .map { it.lastOrNull() ?: Screen.STUDENT_DASHBOARD }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Screen.STUDENT_DASHBOARD)

    // Current User & Auth State
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Language setting
    private val _language = MutableStateFlow(Language.EN)
    val language: StateFlow<Language> = _language.asStateFlow()

    // Demo Data Mode vs Production Data Mode
    private val _isDemoMode = MutableStateFlow(true)
    val isDemoMode: StateFlow<Boolean> = _isDemoMode.asStateFlow()

    // Selected Module & Lesson
    private val _selectedModule = MutableStateFlow<UhvModule?>(null)
    val selectedModule: StateFlow<UhvModule?> = _selectedModule.asStateFlow()

    private val _selectedLesson = MutableStateFlow<UhvLesson?>(null)
    val selectedLesson: StateFlow<UhvLesson?> = _selectedLesson.asStateFlow()

    private val _selectedActivity = MutableStateFlow<PracticeActivity?>(null)
    val selectedActivity: StateFlow<PracticeActivity?> = _selectedActivity.asStateFlow()

    // Quiz State
    private val _currentQuizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val currentQuizQuestions: StateFlow<List<QuizQuestion>> = _currentQuizQuestions.asStateFlow()

    private val _userQuizAnswers = MutableStateFlow<Map<String, Int>>(emptyMap())
    val userQuizAnswers: StateFlow<Map<String, Int>> = _userQuizAnswers.asStateFlow()

    private val _lastQuizResult = MutableStateFlow<UhvRepository.QuizSubmissionResult?>(null)
    val lastQuizResult: StateFlow<UhvRepository.QuizSubmissionResult?> = _lastQuizResult.asStateFlow()

    private val _isSubmittingQuiz = MutableStateFlow(false)
    val isSubmittingQuiz: StateFlow<Boolean> = _isSubmittingQuiz.asStateFlow()

    // Public Verification Search
    private val _searchedCertId = MutableStateFlow("")
    val searchedCertId: StateFlow<String> = _searchedCertId.asStateFlow()

    private val _verificationResult = MutableStateFlow<UhvCertificate?>(null)
    val verificationResult: StateFlow<UhvCertificate?> = _verificationResult.asStateFlow()

    private val _isVerifying = MutableStateFlow(false)
    val isVerifying: StateFlow<Boolean> = _isVerifying.asStateFlow()

    // Student Import State
    private val _importCsvText = MutableStateFlow(InitialDataSeed.getSampleStudentImportData())
    val importCsvText: StateFlow<String> = _importCsvText.asStateFlow()

    private val _importPreview = MutableStateFlow<UhvRepository.ImportPreviewReport?>(null)
    val importPreview: StateFlow<UhvRepository.ImportPreviewReport?> = _importPreview.asStateFlow()

    private val _isImporting = MutableStateFlow(false)
    val isImporting: StateFlow<Boolean> = _isImporting.asStateFlow()

    private val _importSuccessMessage = MutableStateFlow<String?>(null)
    val importSuccessMessage: StateFlow<String?> = _importSuccessMessage.asStateFlow()

    // Repositories Flows
    val allModules: StateFlow<List<UhvModule>> = repository.getAllModules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStudents: StateFlow<List<User>> = repository.getAllStudents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<User>> = repository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val leaderboard: StateFlow<List<User>> = repository.getLeaderboard()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCertificates: StateFlow<List<UhvCertificate>> = repository.getAllCertificates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val announcements: StateFlow<List<Announcement>> = repository.getAllAnnouncements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val events: StateFlow<List<UhvEvent>> = repository.getAllEvents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLog>> = repository.getAuditLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val importRecords: StateFlow<List<ImportRecord>> = repository.getAllImportRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Student Certificates Flow
    val studentCertificates: StateFlow<List<UhvCertificate>> = _currentUser.flatMapLatest { user ->
        if (user != null) repository.getCertificatesForStudent(user.id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Mentor Chat
    data class ChatMessage(val isUser: Boolean, val text: String, val timestamp: Long = System.currentTimeMillis())
    private val _mentorMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                isUser = false,
                text = "Namaste! I am your UHV Mentor for NBKRIST. I can help you understand Universal Human Values, explore harmony in the Self, family, society, and nature, or guide your reflection assignments. How may I assist your self-exploration today?"
            )
        )
    )
    val mentorMessages: StateFlow<List<ChatMessage>> = _mentorMessages.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeIfNeeded()
            // Set default demo student user
            val defaultStudent = repository.getUserById("demo_student_1")
            _currentUser.value = defaultStudent
        }
    }

    // --- Navigation ---
    fun navigateTo(screen: Screen) {
        val current = _screenStack.value.toMutableList()
        current.add(screen)
        _screenStack.value = current
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value.toMutableList()
        if (current.size > 1) {
            current.removeAt(current.size - 1)
            _screenStack.value = current
            return true
        }
        return false
    }

    // --- Language & Mode ---
    fun toggleLanguage() {
        _language.value = if (_language.value == Language.EN) Language.TE else Language.EN
    }

    fun toggleDemoMode() {
        _isDemoMode.value = !_isDemoMode.value
    }

    // --- Role Switching (Demo Accounts) ---
    fun switchUserRole(role: UserRole) {
        viewModelScope.launch {
            val user = when (role) {
                UserRole.STUDENT -> repository.getUserById("demo_student_1")
                UserRole.FACULTY -> repository.getUserById("demo_faculty_1")
                UserRole.ADMIN -> repository.getUserById("demo_admin_1")
                UserRole.SUPER_ADMIN -> repository.getUserById("demo_superadmin_1")
                UserRole.UHV_CELL_MEMBER -> repository.getUserById("demo_admin_1")
            }
            if (user != null) {
                _currentUser.value = user
                repository.insertAuditLog(
                    AuditLog(
                        actorName = user.name,
                        actorRole = user.role.name,
                        action = "ROLE_SWITCH_DEMO",
                        target = "Switched to ${role.name}",
                        result = "SUCCESS"
                    )
                )
                // If switching to Admin / Super Admin, navigate to Admin Dashboard; else Student Dashboard
                if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
                    _screenStack.value = listOf(Screen.ADMIN_DASHBOARD)
                } else if (role == UserRole.FACULTY) {
                    _screenStack.value = listOf(Screen.FACULTY_REVIEW)
                } else {
                    _screenStack.value = listOf(Screen.STUDENT_DASHBOARD)
                }
            }
        }
    }

    fun getLessonsForModule(moduleId: String): Flow<List<UhvLesson>> = repository.getLessonsForModule(moduleId)
    fun getActivitiesForModule(moduleId: String): Flow<List<PracticeActivity>> = repository.getActivitiesForModule(moduleId)
    suspend fun getResponse(studentId: String, activityId: String): ActivityResponse? = repository.getResponse(studentId, activityId)

    // --- Course Navigation ---
    fun openModule(module: UhvModule) {
        _selectedModule.value = module
        navigateTo(Screen.MODULE_DETAIL)
    }

    fun openLesson(lesson: UhvLesson) {
        _selectedLesson.value = lesson
        navigateTo(Screen.LESSON_DETAIL)
    }

    fun markCurrentLessonCompleted() {
        val lesson = _selectedLesson.value ?: return
        val student = _currentUser.value ?: return
        viewModelScope.launch {
            repository.markLessonCompleted(lesson.id, student.id)
            _selectedLesson.value = lesson.copy(isCompleted = true)
            // refresh user
            _currentUser.value = repository.getUserById(student.id)
        }
    }

    fun openPracticeActivity(activity: PracticeActivity) {
        _selectedActivity.value = activity
        navigateTo(Screen.PRACTICE_ACTIVITY)
    }

    fun submitActivityResponse(activityId: String, text: String, onDone: () -> Unit) {
        val student = _currentUser.value ?: return
        viewModelScope.launch {
            repository.saveActivityResponse(activityId, student.id, text)
            _currentUser.value = repository.getUserById(student.id)
            onDone()
        }
    }

    // --- Quiz Engine ---
    fun startModuleQuiz(module: UhvModule) {
        _selectedModule.value = module
        _userQuizAnswers.value = emptyMap()
        _lastQuizResult.value = null
        viewModelScope.launch {
            val questions = repository.getQuestionsForModule(module.id)
            _currentQuizQuestions.value = questions
            navigateTo(Screen.QUIZ)
        }
    }

    fun selectQuizAnswer(questionId: String, optionIndex: Int) {
        val map = _userQuizAnswers.value.toMutableMap()
        map[questionId] = optionIndex
        _userQuizAnswers.value = map
    }

    fun submitQuiz(timeSpentSeconds: Int) {
        val student = _currentUser.value ?: return
        val module = _selectedModule.value ?: return
        _isSubmittingQuiz.value = true
        viewModelScope.launch {
            val result = repository.evaluateQuiz(
                studentId = student.id,
                moduleId = module.id,
                userAnswers = _userQuizAnswers.value,
                timeSpentSeconds = timeSpentSeconds
            )
            _lastQuizResult.value = result
            _isSubmittingQuiz.value = false
            _currentUser.value = repository.getUserById(student.id)
        }
    }

    // --- Certificate Generation ---
    fun requestCertificateForCurrentModule(onSuccess: (UhvCertificate) -> Unit, onError: (String) -> Unit) {
        val student = _currentUser.value ?: return
        val module = _selectedModule.value ?: return
        viewModelScope.launch {
            val cert = repository.issueCertificate(student.id, module.id)
            if (cert != null) {
                _currentUser.value = repository.getUserById(student.id)
                onSuccess(cert)
            } else {
                val eligibility = repository.checkCertificateEligibility(student.id, module.id)
                onError(eligibility.reason)
            }
        }
    }

    // --- Public Certificate Verification ---
    fun setSearchedCertId(id: String) {
        _searchedCertId.value = id
    }

    fun verifyCertificate() {
        val id = _searchedCertId.value.trim()
        if (id.isBlank()) return
        _isVerifying.value = true
        _verificationResult.value = null
        viewModelScope.launch {
            val cert = repository.getCertificateById(id)
            _verificationResult.value = cert
            _isVerifying.value = false
        }
    }

    // --- Admin Certificate Control ---
    fun updateCertificateStatus(certId: String, newStatus: String, reason: String) {
        val admin = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateCertificateStatus(certId, newStatus, reason, admin.name)
        }
    }

    // --- Student Import Pipeline ---
    fun setImportCsvText(text: String) {
        _importCsvText.value = text
    }

    fun parseAndPreviewCsv() {
        viewModelScope.launch {
            val report = repository.processImportCsv(_importCsvText.value)
            _importPreview.value = report
            _importSuccessMessage.value = null
        }
    }

    fun confirmImport() {
        val preview = _importPreview.value ?: return
        val admin = _currentUser.value ?: return
        _isImporting.value = true
        viewModelScope.launch {
            val record = repository.confirmStudentImport(
                report = preview,
                importedBy = admin.name,
                filename = "nbkrist_uhv_import.csv"
            )
            _isImporting.value = false
            _importSuccessMessage.value = "Import successful! ${record.validRows} students added, ${record.needsVerificationRows} sent to verification queue."
            _importPreview.value = null
        }
    }

    // --- AI Mentor Chat ---
    fun sendMentorMessage(userText: String) {
        if (userText.isBlank()) return
        val list = _mentorMessages.value.toMutableList()
        list.add(ChatMessage(isUser = true, text = userText))
        _mentorMessages.value = list

        viewModelScope.launch {
            // Simulated institutional UHV Mentor response based on syllabus topics
            val reply = generateMentorResponse(userText)
            val updated = _mentorMessages.value.toMutableList()
            updated.add(ChatMessage(isUser = false, text = reply))
            _mentorMessages.value = updated
        }
    }

    private fun generateMentorResponse(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("trust") || q.contains("vishwas") ->
                "In UHV, Trust (Vishwas) is the foundational value in relationships. It means having the unconditional assurance that the other person naturally intends my happiness and well-being. Notice the difference between 'Intention' (which is naturally good in everyone) and 'Competence' (which may be lacking). When we separate the two, resentment dissolves."
            q.contains("respect") || q.contains("samman") ->
                "Respect (Samman) means Right Evaluation (Samyak Maan). Disrespect occurs when we over-evaluate, under-evaluate, or evaluate someone based on bodily features, wealth, or caste instead of their conscious Self ('I'). At the level of the Self, every human being has identical natural acceptance."
            q.contains("happiness") || q.contains("prosperity") ->
                "Continuous Happiness is the state of living in harmony at all four levels: within oneself, with family, with society, and with nature. Prosperity is distinct from wealth—it is the feeling of having, or producing, more physical facility than is required for bodily nurturing and protection."
            q.contains("self") || q.contains("body") || q.contains("i and body") ->
                "A human being is the co-existence of the conscious Self ('I') and the material Body. Needs of the Body are quantitative and temporary (food, clothing, shelter), while needs of the Self are qualitative and continuous (trust, respect, harmony). Confusing the two causes consumerist anxiety."
            q.contains("nature") || q.contains("order") ->
                "Nature consists of four mutually fulfilling orders: Material (minerals, soil), Pranic (plants), Animal (animals, birds), and Human (consciousness). While the first three exist in natural cyclicity and balance, human beings can participate constructively only through Right Understanding."
            q.contains("study plan") || q.contains("prepare") ->
                "Recommended Study Plan for UHV Sadhana:\n1. Begin with Module 1: Self Exploration & Natural Acceptance (15 mins/day).\n2. Complete PS 1 & PS 2 reflections to experience inner clarity.\n3. Proceed to Module 2 to distinguish Self ('I') from Body.\n4. Take the Module 1 Check-in Quiz once all lessons show completed."
            else ->
                "That is a deep observation for self-exploration. In UHV, always examine proposals using your own 'Natural Acceptance' (Sahaj Svikriti) rather than accepting external conditioning. Ask yourself: 'Does this action/thought lead to continuous harmony within me and with those around me?'"
        }
    }

    fun registerForEvent(eventId: String) {
        val student = _currentUser.value ?: return
        viewModelScope.launch {
            repository.registerForEvent(eventId, student.id)
            _currentUser.value = repository.getUserById(student.id)
        }
    }
}
