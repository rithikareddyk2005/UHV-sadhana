package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.*

@Database(
    entities = [
        User::class,
        UhvModule::class,
        UhvLesson::class,
        PracticeActivity::class,
        ActivityResponse::class,
        QuizQuestion::class,
        QuizAttempt::class,
        UhvCertificate::class,
        Announcement::class,
        UhvEvent::class,
        AuditLog::class,
        ImportRecord::class,
        DailyUhvReflection::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun quizDao(): QuizDao
    abstract fun certificateDao(): CertificateDao
    abstract fun miscDao(): MiscDao
    abstract fun dailyReflectionDao(): DailyReflectionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "uhv_sadhana_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
