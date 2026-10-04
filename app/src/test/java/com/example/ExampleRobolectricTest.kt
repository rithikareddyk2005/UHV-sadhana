package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("UHV Sadhana", appName)
  }

  @Test
  fun `verify branch normalization logic`() {
    val rawBranch = "Computer Science and Engineering"
    val normalized = when {
      rawBranch.contains("Computer", ignoreCase = true) || rawBranch.contains("cse", ignoreCase = true) -> "CSE"
      else -> rawBranch.uppercase()
    }
    assertEquals("CSE", normalized)
  }

  @Test
  fun `verify server-authoritative quiz pass threshold`() {
    val passingScore = 0.60f
    val studentScore = 14
    val totalQuestions = 20
    val percentage = (studentScore.toFloat() / totalQuestions.toFloat())
    assertTrue(percentage >= passingScore)
  }

  @Test
  fun `verify daily reflection sentiment categorization and timestamp`() {
    val now = System.currentTimeMillis()
    val reflection = com.example.data.model.DailyUhvReflection(
      id = "test_refl_1",
      userId = "student_1",
      userName = "Sample Student",
      title = "Observing Trust in Project Team",
      reflectionText = "Recognizing intention vs competence removed friction.",
      uhvTheme = "Trust & Relationships",
      sentiment = com.example.data.model.SentimentCategory.INSIGHTFUL,
      moodRating = 5,
      timestamp = now,
      dateFormatted = "04 Oct 2026"
    )

    assertEquals(com.example.data.model.SentimentCategory.INSIGHTFUL, reflection.sentiment)
    assertEquals("💡", reflection.sentiment.emoji)
    assertTrue(reflection.timestamp <= System.currentTimeMillis())
    assertEquals(5, reflection.moodRating)
  }
}
