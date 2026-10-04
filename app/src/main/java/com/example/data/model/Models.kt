package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class UserRole {
    STUDENT,
    UHV_CELL_MEMBER,
    FACULTY,
    ADMIN,
    SUPER_ADMIN
}

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val email: String,
    val name: String,
    val rollNo: String = "",
    val role: UserRole = UserRole.STUDENT,
    val branch: String = "CSE",
    val section: String = "A",
    val year: String = "IV",
    val mobile: String = "",
    val profileImage: String = "",
    val xp: Int = 120,
    val streakDays: Int = 3,
    val isDemoAccount: Boolean = true,
    val isVerified: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "modules")
data class UhvModule(
    @PrimaryKey val id: String,
    val orderIndex: Int,
    val titleEn: String,
    val titleTe: String,
    val taglineEn: String,
    val taglineTe: String,
    val description: String,
    val lecturesCount: Int,
    val practiceCount: Int,
    val iconName: String = "menu_book",
    val totalLessons: Int = 5,
    val isUnlocked: Boolean = true
)

@Entity(tableName = "lessons")
data class UhvLesson(
    @PrimaryKey val id: String,
    val moduleId: String,
    val orderIndex: Int,
    val titleEn: String,
    val titleTe: String,
    val description: String,
    val learningObjectives: String, // newline-separated
    val mainContent: String,        // 250-400 words comprehensive text
    val keyTakeaways: String,       // newline-separated
    val campusExample: String,
    val reflectionQuestion: String,
    val durationMinutes: Int = 15,
    val videoTitle: String = "AICTE UHV Video Lecture",
    val videoUrl: String = "https://www.youtube.com/watch?v=sample",
    val videoDurationSec: Int = 720,
    val isCompleted: Boolean = false,
    val isAiGenerated: Boolean = false,
    val reviewStatus: String = "APPROVED" // AI_GENERATED, FACULTY_REVIEW, APPROVED, PUBLISHED
)

@Entity(tableName = "practice_activities")
data class PracticeActivity(
    @PrimaryKey val id: String,
    val moduleId: String,
    val sessionNumber: Int, // PS 1 to PS 14
    val titleEn: String,
    val titleTe: String,
    val activityType: String, // "Self-Observation", "Campus Scenario", "Value-Mapping", "Reflection"
    val scenarioPrompt: String,
    val guidedQuestions: String, // newline-separated
    val expectedOutcome: String
)

@Entity(tableName = "activity_responses")
data class ActivityResponse(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val activityId: String,
    val studentId: String,
    val responseText: String,
    val status: String = "SUBMITTED", // DRAFT, SUBMITTED, REVIEWED
    val facultyFeedback: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_questions")
data class QuizQuestion(
    @PrimaryKey val id: String,
    val moduleId: String,
    val questionEn: String,
    val questionTe: String,
    val optionsJson: String, // JSON array or pipe-separated
    val correctOptionIndex: Int,
    val explanation: String,
    val difficulty: String = "MEDIUM", // EASY, MEDIUM, HARD
    val questionType: String = "MODULE_QUIZ", // CHECK_IN, MODULE_QUIZ, FINAL
    val reviewStatus: String = "APPROVED" // AI_GENERATED, FACULTY_REVIEW, APPROVED
)

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val studentId: String,
    val moduleId: String,
    val attemptNumber: Int,
    val totalQuestions: Int,
    val score: Int,
    val percentage: Float,
    val passed: Boolean,
    val timeSpentSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "certificates")
data class UhvCertificate(
    @PrimaryKey val certificateId: String, // e.g. UHV-2026-7F3K9P2M
    val studentId: String,
    val studentName: String,
    val rollNo: String,
    val moduleId: String,
    val moduleTitle: String,
    val issueDate: String,
    val issueTimestamp: Long = System.currentTimeMillis(),
    val status: String = "VALID", // VALID, REVOKED
    val revocationReason: String = "",
    val institutionalAuthority: String = "NBKRIST UHV Cell Authority",
    val verificationHash: String = UUID.randomUUID().toString().take(12)
)

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val titleEn: String,
    val titleTe: String,
    val content: String,
    val priority: String = "NORMAL", // HIGH, NORMAL
    val targetAudience: String = "ALL", // ALL, CSE, ECE, YEAR_IV
    val datePosted: String,
    val isPinned: Boolean = false,
    val isRead: Boolean = false
)

@Entity(tableName = "events")
data class UhvEvent(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val titleEn: String,
    val titleTe: String,
    val description: String,
    val eventDate: String,
    val eventTime: String,
    val venue: String,
    val organizer: String = "UHV Cell, NBKRIST",
    val registrationLink: String = "https://nbkrist.org/uhv-events",
    val isRegistered: Boolean = false
)

@Entity(tableName = "audit_logs")
data class AuditLog(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val actorName: String,
    val actorRole: String,
    val action: String,
    val target: String,
    val result: String = "SUCCESS",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "import_records")
data class ImportRecord(
    @PrimaryKey val importId: String = UUID.randomUUID().toString(),
    val filename: String,
    val totalRows: Int,
    val validRows: Int,
    val duplicateRows: Int,
    val needsVerificationRows: Int,
    val importedBy: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "COMPLETED"
)

data class StudentImportRow(
    val fullName: String,
    val rollNo: String,
    val branch: String,
    val section: String,
    val year: String,
    val mobile: String,
    val email: String,
    val status: String = "VALID", // VALID, DUPLICATE, NEEDS_VERIFICATION
    val issues: String = ""
)

enum class SentimentCategory(val displayName: String, val emoji: String) {
    HARMONIOUS("Harmonious & Peaceful", "🌿"),
    GRATITUDE("Deep Gratitude", "🙏"),
    INSIGHTFUL("Self-Realization & Clarity", "💡"),
    CONTEMPLATIVE("Contemplative & Seeking", "🤔"),
    CHALLENGED("Challenged & Resolving", "🌱")
}

@Entity(tableName = "daily_reflections")
data class DailyUhvReflection(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val userName: String,
    val title: String,
    val reflectionText: String,
    val uhvTheme: String, // e.g. "Harmony in Self", "Trust & Relationships", "Harmony in Nature", "Professional Ethics"
    val sentiment: SentimentCategory = SentimentCategory.HARMONIOUS,
    val moodRating: Int = 5, // 1 to 5 scale
    val timestamp: Long = System.currentTimeMillis(),
    val dateFormatted: String = ""
)

