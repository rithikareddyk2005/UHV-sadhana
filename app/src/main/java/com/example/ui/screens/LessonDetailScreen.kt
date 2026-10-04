package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UhvLesson
import com.example.ui.components.UhvResourceVideoPlayer
import com.example.ui.theme.*
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.UhvViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    viewModel: UhvViewModel,
    lesson: UhvLesson,
    language: Language
) {
    val context = LocalContext.current
    BackHandler { viewModel.navigateBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lecture ${lesson.orderIndex}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("lesson_detail_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .testTag("lesson_detail_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Lesson Title & Duration Header
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = UhvNavyPrimary.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "Estimated: ${lesson.durationMinutes} Mins",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = UhvNavyPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            if (lesson.isCompleted) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = UhvSuccess.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "Completed ✓",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = UhvSuccess,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (language == Language.EN) lesson.titleEn else lesson.titleTe,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = lesson.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Learning Objectives Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrackChanges, contentDescription = null, tint = UhvNavyPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == Language.EN) "Learning Objectives" else "అభ్యాస లక్ష్యాలు",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        lesson.learningObjectives.lines().filter { it.isNotBlank() }.forEach { obj ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("• ", fontWeight = FontWeight.Bold, color = UhvNavyPrimary)
                                Text(
                                    text = obj.trim(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Video Player playing directly from resources
            item {
                val videoResId = if (lesson.orderIndex % 2 != 0) R.raw.uhv_lecture_1 else R.raw.uhv_lecture_2
                UhvResourceVideoPlayer(
                    rawResourceId = videoResId,
                    title = "Lecture ${lesson.orderIndex}: ${lesson.videoTitle}",
                    onWatchProgressUpdated = { progress ->
                        // Continuous watch progress tracking from raw video resource
                    }
                )
            }

            // Main Content (250-400 words)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Article, contentDescription = null, tint = UhvNavyPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == Language.EN) "Academic Study Content" else "విద్యా అధ్యయన విషయము",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = lesson.mainContent,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Key Takeaways
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UhvGoldAccent.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = UhvGoldAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == Language.EN) "Key Takeaways" else "ముఖ్యమైన అంశాలు",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        lesson.keyTakeaways.lines().filter { it.isNotBlank() }.forEach { takeaway ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("✓ ", fontWeight = FontWeight.Bold, color = UhvNavyPrimary)
                                Text(
                                    text = takeaway.trim(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Campus Example Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, contentDescription = null, tint = UhvNavyPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == Language.EN) "NBKRIST Campus Example" else "క్యాంపస్ ఉదాహరణ",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = lesson.campusExample,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Reflection Question Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = UhvNavyPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == Language.EN) "Self-Exploration Reflection" else "స్వీయ ప్రతిబింబన ప్రశ్న",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = lesson.reflectionQuestion,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Mark as Completed Button
            item {
                Button(
                    onClick = {
                        viewModel.markCurrentLessonCompleted()
                        Toast.makeText(context, "Lesson completed! +25 XP awarded.", Toast.LENGTH_SHORT).show()
                    },
                    enabled = !lesson.isCompleted,
                    colors = ButtonDefaults.buttonColors(containerColor = UhvNavyPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("mark_lesson_completed_button")
                ) {
                    Icon(
                        imageVector = if (lesson.isCompleted) Icons.Default.Check else Icons.Default.DoneAll,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (lesson.isCompleted) "Completed (+25 XP)" else "Mark as Completed (+25 XP)",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
