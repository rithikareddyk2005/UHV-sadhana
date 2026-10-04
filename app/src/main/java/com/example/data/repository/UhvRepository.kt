package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.InitialDataSeed
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.*

class UhvRepository(private val db: AppDatabase) {

    private val userDao = db.userDao()
    private val courseDao = db.courseDao()
    private val quizDao = db.quizDao()
    private val certificateDao = db.certificateDao()
    private val miscDao = db.miscDao()
    private val dailyReflectionDao = db.dailyReflectionDao()

    suspend fun initializeIfNeeded() = withContext(Dispatchers.IO) {
        val modules = courseDao.getAllModules().firstOrNull()
        if (modules.isNullOrEmpty()) {
            courseDao.insertModules(InitialDataSeed.getModules())
            courseDao.insertLessons(InitialDataSeed.getLessons())
            courseDao.insertActivities(InitialDataSeed.getPracticeActivities())
            quizDao.insertQuestions(InitialDataSeed.getQuestions())
            userDao.insertUsers(InitialDataSeed.getDemoUsers())
            certificateDao.insertCertificate(InitialDataSeed.getSampleCertificates().first())
            miscDao.insertAnnouncements(InitialDataSeed.getAnnouncements())
            miscDao.insertEvents(InitialDataSeed.getEvents())
            for (log in InitialDataSeed.getSampleAuditLogs()) {
                miscDao.insertAuditLog(log)
            }
            for (refl in InitialDataSeed.getSampleReflections()) {
                dailyReflectionDao.insertReflection(refl)
            }
        }
    }

    // --- Users & Roles ---
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    fun getAllStudents(): Flow<List<User>> = userDao.getAllStudents()
    fun getLeaderboard(): Flow<List<User>> = userDao.getLeaderboard()

    suspend fun getUserById(userId: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserById(userId)
    }

    suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByEmail(email)
    }

    suspend fun getUserByRollNo(rollNo: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByRollNo(rollNo)
    }

    suspend fun insertUser(user: User) = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
    }

    suspend fun addXp(userId: String, points: Int) = withContext(Dispatchers.IO) {
        userDao.addXp(userId, points)
    }

    // --- Course & Lessons ---
    fun getAllModules(): Flow<List<UhvModule>> = courseDao.getAllModules()

    suspend fun getModuleById(moduleId: String): UhvModule? = withContext(Dispatchers.IO) {
        courseDao.getModuleById(moduleId)
    }

    fun getLessonsForModule(moduleId: String): Flow<List<UhvLesson>> =
        courseDao.getLessonsForModule(moduleId)

    suspend fun getLessonById(lessonId: String): UhvLesson? = withContext(Dispatchers.IO) {
        courseDao.getLessonById(lessonId)
    }

    suspend fun markLessonCompleted(lessonId: String, studentId: String) = withContext(Dispatchers.IO) {
        courseDao.markLessonCompleted(lessonId)
        userDao.addXp(studentId, 25)
        miscDao.insertAuditLog(
            AuditLog(
                actorName = studentId,
                actorRole = "STUDENT",
                action = "LESSON_COMPLETED",
                target = lessonId
            )
        )
    }

    fun getActivitiesForModule(moduleId: String): Flow<List<PracticeActivity>> =
        courseDao.getActivitiesForModule(moduleId)

    suspend fun getResponse(studentId: String, activityId: String): ActivityResponse? = withContext(Dispatchers.IO) {
        courseDao.getResponse(studentId, activityId)
    }

    fun getAllResponsesForStudent(studentId: String): Flow<List<ActivityResponse>> =
        courseDao.getAllResponsesForStudent(studentId)

    suspend fun saveActivityResponse(
        activityId: String,
        studentId: String,
        text: String
    ) = withContext(Dispatchers.IO) {
        val existing = courseDao.getResponse(studentId, activityId)
        val response = ActivityResponse(
            id = existing?.id ?: UUID.randomUUID().toString(),
            activityId = activityId,
            studentId = studentId,
            responseText = text,
            status = "SUBMITTED",
            updatedAt = System.currentTimeMillis()
        )
        courseDao.saveActivityResponse(response)
        userDao.addXp(studentId, 30)
        miscDao.insertAuditLog(
            AuditLog(
                actorName = studentId,
                actorRole = "STUDENT",
                action = "PRACTICE_ACTIVITY_SUBMITTED",
                target = activityId
            )
        )
    }

    // --- Server-Authoritative Quiz Engine ---
    suspend fun getQuestionsForModule(moduleId: String): List<QuizQuestion> = withContext(Dispatchers.IO) {
        quizDao.getQuestionsForModule(moduleId)
    }

    fun getAttemptsForModule(studentId: String, moduleId: String): Flow<List<QuizAttempt>> =
        quizDao.getAttemptsForModule(studentId, moduleId)

    fun getAllRecentAttempts(): Flow<List<QuizAttempt>> = quizDao.getAllRecentAttempts()

    data class QuizSubmissionResult(
        val attempt: QuizAttempt,
        val totalQuestions: Int,
        val correctCount: Int,
        val percentage: Float,
        val passed: Boolean,
        val eligibleForCertificate: Boolean,
        val message: String
    )

    suspend fun evaluateQuiz(
        studentId: String,
        moduleId: String,
        userAnswers: Map<String, Int>, // questionId -> selectedOptionIndex
        timeSpentSeconds: Int
    ): QuizSubmissionResult = withContext(Dispatchers.IO) {
        val questions = quizDao.getQuestionsForModule(moduleId)
        val attemptCount = quizDao.getAttemptCount(studentId, moduleId)

        if (attemptCount >= 3) {
            val lastAttempt = quizDao.getAttemptsForModule(studentId, moduleId).firstOrNull()?.firstOrNull()
            if (lastAttempt != null && (System.currentTimeMillis() - lastAttempt.timestamp) < 24 * 60 * 60 * 1000) {
                return@withContext QuizSubmissionResult(
                    attempt = lastAttempt,
                    totalQuestions = questions.size,
                    correctCount = lastAttempt.score,
                    percentage = lastAttempt.percentage,
                    passed = lastAttempt.passed,
                    eligibleForCertificate = false,
                    message = "Maximum 3 attempts reached. Cooldown active for 24 hours."
                )
            }
        }

        var correctCount = 0
        for (q in questions) {
            val userAns = userAnswers[q.id]
            if (userAns != null && userAns == q.correctOptionIndex) {
                correctCount++
            }
        }

        val total = questions.size.coerceAtLeast(1)
        val percentage = (correctCount.toFloat() / total.toFloat()) * 100f
        val passed = percentage >= 60.0f

        val attempt = QuizAttempt(
            studentId = studentId,
            moduleId = moduleId,
            attemptNumber = attemptCount + 1,
            totalQuestions = total,
            score = correctCount,
            percentage = percentage,
            passed = passed,
            timeSpentSeconds = timeSpentSeconds,
            timestamp = System.currentTimeMillis()
        )
        quizDao.saveQuizAttempt(attempt)

        val xpBonus = if (passed) 50 else 15
        userDao.addXp(studentId, xpBonus)

        miscDao.insertAuditLog(
            AuditLog(
                actorName = studentId,
                actorRole = "STUDENT",
                action = "QUIZ_EVALUATION_SERVER",
                target = "Module $moduleId (Score: $correctCount/$total, Passed: $passed)",
                result = if (passed) "PASS" else "FAIL"
            )
        )

        // Check if eligible for certificate
        val eligibility = checkCertificateEligibility(studentId, moduleId)

        QuizSubmissionResult(
            attempt = attempt,
            totalQuestions = total,
            correctCount = correctCount,
            percentage = percentage,
            passed = passed,
            eligibleForCertificate = eligibility.eligible,
            message = if (passed) "Congratulations! You passed the module assessment." else "Assessment incomplete. Passing score is 60%."
        )
    }

    // --- Certificate Engine ---
    data class EligibilityStatus(
        val eligible: Boolean,
        val lessonsDone: Boolean,
        val activitiesDone: Boolean,
        val quizPassed: Boolean,
        val completedLessons: Int,
        val totalLessons: Int,
        val completedActivities: Int,
        val totalActivities: Int,
        val reason: String
    )

    suspend fun checkCertificateEligibility(studentId: String, moduleId: String): EligibilityStatus = withContext(Dispatchers.IO) {
        val completedLessons = courseDao.getCompletedLessonsCount(moduleId)
        val totalLessons = courseDao.getTotalLessonsCount(moduleId)
        val completedActivities = courseDao.getCompletedActivitiesCount(studentId, moduleId)
        val totalActivities = courseDao.getActivitiesForModule(moduleId).firstOrNull()?.size ?: 0
        val passedAttempt = quizDao.getPassedAttempt(studentId, moduleId)

        val lessonsDone = totalLessons > 0 && completedLessons >= (totalLessons * 0.75).toInt()
        val activitiesDone = totalActivities == 0 || completedActivities >= 1
        val quizPassed = passedAttempt != null

        val eligible = lessonsDone && activitiesDone && quizPassed
        val reason = when {
            !lessonsDone -> "Complete more lessons for this module."
            !activitiesDone -> "Submit practice activities to qualify."
            !quizPassed -> "Pass the module quiz with >= 60%."
            else -> "All requirements satisfied."
        }

        EligibilityStatus(
            eligible = eligible,
            lessonsDone = lessonsDone,
            activitiesDone = activitiesDone,
            quizPassed = quizPassed,
            completedLessons = completedLessons,
            totalLessons = totalLessons,
            completedActivities = completedActivities,
            totalActivities = totalActivities,
            reason = reason
        )
    }

    suspend fun issueCertificate(studentId: String, moduleId: String): UhvCertificate? = withContext(Dispatchers.IO) {
        val eligibility = checkCertificateEligibility(studentId, moduleId)
        if (!eligibility.eligible) {
            return@withContext null
        }

        val student = userDao.getUserById(studentId) ?: return@withContext null
        val module = courseDao.getModuleById(moduleId) ?: return@withContext null

        val secureId = generateSecureCertificateId()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        val issueDate = dateFormat.format(Date())

        val certificate = UhvCertificate(
            certificateId = secureId,
            studentId = student.id,
            studentName = student.name,
            rollNo = student.rollNo,
            moduleId = moduleId,
            moduleTitle = module.titleEn,
            issueDate = issueDate,
            status = "VALID",
            institutionalAuthority = "NBKRIST UHV Cell Authority",
            verificationHash = UUID.randomUUID().toString().take(12).uppercase()
        )

        certificateDao.insertCertificate(certificate)
        userDao.addXp(studentId, 100)

        miscDao.insertAuditLog(
            AuditLog(
                actorName = "Certificate Engine",
                actorRole = "SYSTEM",
                action = "CERTIFICATE_ISSUED",
                target = "${student.name} (${student.rollNo}) - $secureId",
                result = "SUCCESS"
            )
        )

        certificate
    }

    private fun generateSecureCertificateId(): String {
        val allowedChars = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ"
        val random = SecureRandom()
        val randomPart = (1..8)
            .map { allowedChars[random.nextInt(allowedChars.length)] }
            .joinToString("")
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return "UHV-$year-$randomPart"
    }

    fun getCertificatesForStudent(studentId: String): Flow<List<UhvCertificate>> =
        certificateDao.getCertificatesForStudent(studentId)

    fun getAllCertificates(): Flow<List<UhvCertificate>> = certificateDao.getAllCertificates()

    suspend fun getCertificateById(certId: String): UhvCertificate? = withContext(Dispatchers.IO) {
        certificateDao.getCertificateById(certId.trim().uppercase())
    }

    suspend fun updateCertificateStatus(
        certId: String,
        newStatus: String,
        reason: String,
        adminName: String
    ) = withContext(Dispatchers.IO) {
        certificateDao.updateCertificateStatus(certId, newStatus, reason)
        miscDao.insertAuditLog(
            AuditLog(
                actorName = adminName,
                actorRole = "ADMIN",
                action = "CERTIFICATE_STATUS_CHANGED",
                target = "$certId to $newStatus (Reason: $reason)",
                result = "SUCCESS"
            )
        )
    }

    // --- Real Student Data Import Center ---
    data class ImportPreviewReport(
        val totalRows: Int,
        val validRows: Int,
        val duplicateRows: Int,
        val needsVerificationRows: Int,
        val parsedStudents: List<StudentImportRow>,
        val issuesSummary: List<String>
    )

    suspend fun processImportCsv(csvContent: String): ImportPreviewReport = withContext(Dispatchers.IO) {
        val lines = csvContent.lines().filter { it.isNotBlank() }
        if (lines.size <= 1) {
            return@withContext ImportPreviewReport(0, 0, 0, 0, emptyList(), listOf("CSV is empty or missing headers."))
        }

        val header = lines.first().split(",").map { it.trim().uppercase() }
        val nameIndex = header.indexOfFirst { it.contains("NAME") }
        val rollIndex = header.indexOfFirst { it.contains("ROLL") }
        val branchIndex = header.indexOfFirst { it.contains("BRANCH") }
        val sectionIndex = header.indexOfFirst { it.contains("SECTION") }
        val yearIndex = header.indexOfFirst { it.contains("YEAR") }
        val mobileIndex = header.indexOfFirst { it.contains("MOBILE") }
        val emailIndex = header.indexOfFirst { it.contains("EMAIL") }

        val parsedList = mutableListOf<StudentImportRow>()
        val seenRolls = mutableSetOf<String>()
        val existingStudents = userDao.getAllStudents().firstOrNull().orEmpty().map { it.rollNo.uppercase() }.toSet()

        var validCount = 0
        var dupCount = 0
        var needsVerificationCount = 0
        val issues = mutableListOf<String>()

        for (i in 1 until lines.size) {
            val parts = lines[i].split(",").map { it.trim() }
            val name = if (nameIndex in parts.indices) parts[nameIndex] else "Unknown"
            val rawRoll = if (rollIndex in parts.indices) parts[rollIndex] else ""
            val rawBranch = if (branchIndex in parts.indices) parts[branchIndex] else "CSE"
            val rawSection = if (sectionIndex in parts.indices) parts[sectionIndex] else "A"
            val rawYear = if (yearIndex in parts.indices) parts[yearIndex] else "I"
            val mobile = if (mobileIndex in parts.indices) parts[mobileIndex] else ""
            val email = if (emailIndex in parts.indices) parts[emailIndex] else ""

            val roll = rawRoll.uppercase().replace("\\s+".toRegex(), "")
            val branch = normalizeBranch(rawBranch)
            val section = rawSection.uppercase().replace("SECTION", "").trim().take(2).ifEmpty { "A" }
            val year = normalizeYear(rawYear)

            val studentIssues = mutableListOf<String>()
            var status = "VALID"

            if (roll.isBlank() || roll.length < 5) {
                status = "NEEDS_VERIFICATION"
                studentIssues.add("Missing or malformed Roll Number")
            } else if (!email.contains("@")) {
                status = "NEEDS_VERIFICATION"
                studentIssues.add("Invalid email address format")
            } else if (seenRolls.contains(roll) || existingStudents.contains(roll)) {
                status = "DUPLICATE"
                studentIssues.add("Roll Number already registered")
            }

            seenRolls.add(roll)

            when (status) {
                "VALID" -> validCount++
                "DUPLICATE" -> dupCount++
                "NEEDS_VERIFICATION" -> {
                    needsVerificationCount++
                    issues.add("Row $i ($name): ${studentIssues.joinToString(", ")}")
                }
            }

            parsedList.add(
                StudentImportRow(
                    fullName = name,
                    rollNo = roll,
                    branch = branch,
                    section = section,
                    year = year,
                    mobile = mobile,
                    email = email,
                    status = status,
                    issues = studentIssues.joinToString("; ")
                )
            )
        }

        ImportPreviewReport(
            totalRows = lines.size - 1,
            validRows = validCount,
            duplicateRows = dupCount,
            needsVerificationRows = needsVerificationCount,
            parsedStudents = parsedList,
            issuesSummary = issues
        )
    }

    suspend fun confirmStudentImport(
        report: ImportPreviewReport,
        importedBy: String,
        filename: String = "nbkrist_students.csv"
    ): ImportRecord = withContext(Dispatchers.IO) {
        val validToInsert = report.parsedStudents.filter { it.status == "VALID" }.map {
            User(
                id = UUID.randomUUID().toString(),
                email = it.email,
                name = it.fullName,
                rollNo = it.rollNo,
                role = UserRole.STUDENT,
                branch = it.branch,
                section = it.section,
                year = it.year,
                mobile = it.mobile,
                xp = 100,
                streakDays = 1,
                isDemoAccount = false,
                isVerified = true
            )
        }

        userDao.insertUsers(validToInsert)

        val record = ImportRecord(
            filename = filename,
            totalRows = report.totalRows,
            validRows = report.validRows,
            duplicateRows = report.duplicateRows,
            needsVerificationRows = report.needsVerificationRows,
            importedBy = importedBy,
            status = "COMPLETED"
        )
        miscDao.insertImportRecord(record)

        miscDao.insertAuditLog(
            AuditLog(
                actorName = importedBy,
                actorRole = "ADMIN",
                action = "STUDENT_DATA_IMPORTED",
                target = "$filename (${report.validRows} students added, ${report.needsVerificationRows} flagged)",
                result = "SUCCESS"
            )
        )

        record
    }

    private fun normalizeBranch(raw: String): String {
        val s = raw.lowercase().trim()
        return when {
            s.contains("computer") || s.contains("cse") -> "CSE"
            s.contains("electronic") || s.contains("ece") -> "ECE"
            s.contains("electrical") || s.contains("eee") -> "EEE"
            s.contains("mech") -> "MECH"
            s.contains("civil") -> "CIVIL"
            s.contains("ai") || s.contains("data") -> "AI & DS"
            else -> raw.uppercase()
        }
    }

    private fun normalizeYear(raw: String): String {
        val s = raw.uppercase().trim()
        return when {
            s == "1" || s == "I" || s.contains("1ST") -> "I"
            s == "2" || s == "II" || s.contains("2ND") -> "II"
            s == "3" || s == "III" || s.contains("3RD") -> "III"
            s == "4" || s == "IV" || s.contains("4TH") -> "IV"
            else -> raw
        }
    }

    // --- Misc: Announcements, Events, Audit Logs ---
    fun getAllAnnouncements(): Flow<List<Announcement>> = miscDao.getAllAnnouncements()
    fun getAllEvents(): Flow<List<UhvEvent>> = miscDao.getAllEvents()
    fun getAuditLogs(): Flow<List<AuditLog>> = miscDao.getAuditLogs()
    fun getAllImportRecords(): Flow<List<ImportRecord>> = miscDao.getAllImportRecords()

    suspend fun registerForEvent(eventId: String, studentId: String) = withContext(Dispatchers.IO) {
        miscDao.registerForEvent(eventId)
        userDao.addXp(studentId, 15)
        miscDao.insertAuditLog(
            AuditLog(
                actorName = studentId,
                actorRole = "STUDENT",
                action = "EVENT_REGISTRATION",
                target = eventId
            )
        )
    }

    suspend fun insertAuditLog(log: AuditLog) = withContext(Dispatchers.IO) {
        miscDao.insertAuditLog(log)
    }

    // --- Daily UHV Reflections ---
    fun getAllDailyReflections(): Flow<List<DailyUhvReflection>> = dailyReflectionDao.getAllReflections()

    fun getDailyReflectionsForUser(userId: String): Flow<List<DailyUhvReflection>> =
        dailyReflectionDao.getReflectionsForUser(userId)

    fun getDailyReflectionsBySentiment(userId: String, sentiment: SentimentCategory): Flow<List<DailyUhvReflection>> =
        dailyReflectionDao.getReflectionsBySentiment(userId, sentiment)

    suspend fun saveDailyReflection(reflection: DailyUhvReflection) = withContext(Dispatchers.IO) {
        dailyReflectionDao.insertReflection(reflection)
        userDao.addXp(reflection.userId, 20)
        miscDao.insertAuditLog(
            AuditLog(
                actorName = reflection.userName,
                actorRole = "STUDENT",
                action = "DAILY_REFLECTION_RECORDED",
                target = "${reflection.title} (${reflection.sentiment.displayName})",
                result = "SUCCESS"
            )
        )
    }

    suspend fun deleteDailyReflection(id: String) = withContext(Dispatchers.IO) {
        dailyReflectionDao.deleteReflectionById(id)
    }
}
