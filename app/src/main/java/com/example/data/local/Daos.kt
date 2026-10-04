package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE role = 'STUDENT' ORDER BY rollNo ASC")
    fun getAllStudents(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE rollNo = :rollNo LIMIT 1")
    suspend fun getUserByRollNo(rollNo: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET xp = xp + :points WHERE id = :userId")
    suspend fun addXp(userId: String, points: Int)

    @Query("SELECT * FROM users WHERE role = 'STUDENT' ORDER BY xp DESC LIMIT 20")
    fun getLeaderboard(): Flow<List<User>>
}

@Dao
interface CourseDao {
    @Query("SELECT * FROM modules ORDER BY orderIndex ASC")
    fun getAllModules(): Flow<List<UhvModule>>

    @Query("SELECT * FROM modules WHERE id = :moduleId LIMIT 1")
    suspend fun getModuleById(moduleId: String): UhvModule?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModules(modules: List<UhvModule>)

    @Query("SELECT * FROM lessons WHERE moduleId = :moduleId ORDER BY orderIndex ASC")
    fun getLessonsForModule(moduleId: String): Flow<List<UhvLesson>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId LIMIT 1")
    suspend fun getLessonById(lessonId: String): UhvLesson?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<UhvLesson>)

    @Query("UPDATE lessons SET isCompleted = 1 WHERE id = :lessonId")
    suspend fun markLessonCompleted(lessonId: String)

    @Query("SELECT COUNT(*) FROM lessons WHERE moduleId = :moduleId AND isCompleted = 1")
    suspend fun getCompletedLessonsCount(moduleId: String): Int

    @Query("SELECT COUNT(*) FROM lessons WHERE moduleId = :moduleId")
    suspend fun getTotalLessonsCount(moduleId: String): Int

    @Query("SELECT * FROM practice_activities WHERE moduleId = :moduleId ORDER BY sessionNumber ASC")
    fun getActivitiesForModule(moduleId: String): Flow<List<PracticeActivity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<PracticeActivity>)

    @Query("SELECT * FROM activity_responses WHERE studentId = :studentId AND activityId = :activityId LIMIT 1")
    suspend fun getResponse(studentId: String, activityId: String): ActivityResponse?

    @Query("SELECT * FROM activity_responses WHERE studentId = :studentId")
    fun getAllResponsesForStudent(studentId: String): Flow<List<ActivityResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActivityResponse(response: ActivityResponse)

    @Query("SELECT COUNT(*) FROM activity_responses WHERE studentId = :studentId AND activityId IN (SELECT id FROM practice_activities WHERE moduleId = :moduleId)")
    suspend fun getCompletedActivitiesCount(studentId: String, moduleId: String): Int
}

@Dao
interface QuizDao {
    @Query("SELECT * FROM quiz_questions WHERE moduleId = :moduleId ORDER BY id ASC")
    suspend fun getQuestionsForModule(moduleId: String): List<QuizQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQuizAttempt(attempt: QuizAttempt)

    @Query("SELECT * FROM quiz_attempts WHERE studentId = :studentId AND moduleId = :moduleId ORDER BY timestamp DESC")
    fun getAttemptsForModule(studentId: String, moduleId: String): Flow<List<QuizAttempt>>

    @Query("SELECT COUNT(*) FROM quiz_attempts WHERE studentId = :studentId AND moduleId = :moduleId")
    suspend fun getAttemptCount(studentId: String, moduleId: String): Int

    @Query("SELECT * FROM quiz_attempts WHERE studentId = :studentId AND moduleId = :moduleId AND passed = 1 LIMIT 1")
    suspend fun getPassedAttempt(studentId: String, moduleId: String): QuizAttempt?

    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC LIMIT 50")
    fun getAllRecentAttempts(): Flow<List<QuizAttempt>>
}

@Dao
interface CertificateDao {
    @Query("SELECT * FROM certificates WHERE studentId = :studentId ORDER BY issueTimestamp DESC")
    fun getCertificatesForStudent(studentId: String): Flow<List<UhvCertificate>>

    @Query("SELECT * FROM certificates ORDER BY issueTimestamp DESC")
    fun getAllCertificates(): Flow<List<UhvCertificate>>

    @Query("SELECT * FROM certificates WHERE certificateId = :certId LIMIT 1")
    suspend fun getCertificateById(certId: String): UhvCertificate?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCertificate(certificate: UhvCertificate)

    @Query("UPDATE certificates SET status = :status, revocationReason = :reason WHERE certificateId = :certId")
    suspend fun updateCertificateStatus(certId: String, status: String, reason: String)
}

@Dao
interface MiscDao {
    @Query("SELECT * FROM announcements ORDER BY isPinned DESC, datePosted DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(items: List<Announcement>)

    @Query("SELECT * FROM events ORDER BY eventDate ASC")
    fun getAllEvents(): Flow<List<UhvEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(items: List<UhvEvent>)

    @Query("UPDATE events SET isRegistered = 1 WHERE id = :eventId")
    suspend fun registerForEvent(eventId: String)

    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 100")
    fun getAuditLogs(): Flow<List<AuditLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLog)

    @Query("SELECT * FROM import_records ORDER BY timestamp DESC")
    fun getAllImportRecords(): Flow<List<ImportRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImportRecord(record: ImportRecord)
}

@Dao
interface DailyReflectionDao {
    @Query("SELECT * FROM daily_reflections ORDER BY timestamp DESC")
    fun getAllReflections(): Flow<List<DailyUhvReflection>>

    @Query("SELECT * FROM daily_reflections WHERE userId = :userId ORDER BY timestamp DESC")
    fun getReflectionsForUser(userId: String): Flow<List<DailyUhvReflection>>

    @Query("SELECT * FROM daily_reflections WHERE userId = :userId AND sentiment = :sentiment ORDER BY timestamp DESC")
    fun getReflectionsBySentiment(userId: String, sentiment: SentimentCategory): Flow<List<DailyUhvReflection>>

    @Query("SELECT * FROM daily_reflections WHERE id = :id LIMIT 1")
    suspend fun getReflectionById(id: String): DailyUhvReflection?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(reflection: DailyUhvReflection)

    @Update
    suspend fun updateReflection(reflection: DailyUhvReflection)

    @Query("DELETE FROM daily_reflections WHERE id = :id")
    suspend fun deleteReflectionById(id: String)

    @Query("SELECT COUNT(*) FROM daily_reflections WHERE userId = :userId")
    suspend fun getReflectionCountForUser(userId: String): Int
}

