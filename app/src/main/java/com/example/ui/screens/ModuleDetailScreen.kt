package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PracticeActivity
import com.example.data.model.UhvLesson
import com.example.data.model.UhvModule
import com.example.ui.theme.*
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UhvViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleDetailScreen(
    viewModel: UhvViewModel,
    module: UhvModule,
    language: Language
) {
    val context = LocalContext.current
    BackHandler { viewModel.navigateBack() }

    var lessons by remember { mutableStateOf<List<UhvLesson>>(emptyList()) }
    var activities by remember { mutableStateOf<List<PracticeActivity>>(emptyList()) }

    LaunchedEffect(module.id) {
        viewModel.getLessonsForModule(module.id).collectLatest { lessons = it }
    }
    LaunchedEffect(module.id) {
        viewModel.getActivitiesForModule(module.id).collectLatest { activities = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Module ${module.orderIndex}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("module_detail_back_button")
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
                .testTag("module_detail_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Module Header Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = UhvNavyPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = if (language == Language.EN) module.titleEn else module.titleTe,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (language == Language.EN) module.taglineEn else module.taglineTe,
                            style = MaterialTheme.typography.bodyMedium,
                            color = UhvGoldLight
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = module.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Section: Structured Lectures & Lessons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == Language.EN) "Curriculum Lectures & Lessons" else "పాఠాలు మరియు ఉపన్యాసాలు",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${lessons.count { it.isCompleted }}/${lessons.size} Done",
                        style = MaterialTheme.typography.labelSmall,
                        color = UhvSuccess,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(lessons) { lesson ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.openLesson(lesson) }
                        .testTag("lesson_item_${lesson.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (lesson.isCompleted) UhvSuccess.copy(alpha = 0.15f) else UhvNavyPrimary.copy(alpha = 0.1f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (lesson.isCompleted) Icons.Default.CheckCircle else Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = if (lesson.isCompleted) UhvSuccess else UhvNavyPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Lecture ${lesson.orderIndex}: ${if (language == Language.EN) lesson.titleEn else lesson.titleTe}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${lesson.durationMinutes} mins • Video + Academic Content",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "Open",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Section: Practice Sessions
            item {
                Text(
                    text = if (language == Language.EN) "Practice Sessions (PS)" else "ప్రాక్టీస్ సెషన్లు",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            items(activities) { activity ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.openPracticeActivity(activity) }
                        .testTag("activity_item_${activity.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = UhvGoldAccent.copy(alpha = 0.15f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.EditNote,
                                    contentDescription = null,
                                    tint = UhvGoldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == Language.EN) activity.titleEn else activity.titleTe,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Type: ${activity.activityType} • Reflection Journal",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "Open",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Section: Module Assessment & Certification
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Module Assessment & Certification",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = UhvGoldAccent)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Passing Score: 60%\n• Authoritative Server-side evaluation\n• Shuffled questions with 30-min timer\n• Completes eligibility for Verifiable Certificate",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.startModuleQuiz(module) },
                                colors = ButtonDefaults.buttonColors(containerColor = UhvNavyPrimary),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("start_module_quiz_button")
                            ) {
                                Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Take Quiz", fontWeight = FontWeight.Bold)
                            }

                            FilledTonalButton(
                                onClick = {
                                    viewModel.requestCertificateForCurrentModule(
                                        onSuccess = { cert ->
                                            Toast.makeText(context, "Certificate Issued: ${cert.certificateId}", Toast.LENGTH_LONG).show()
                                            viewModel.navigateTo(Screen.CERTIFICATES)
                                        },
                                        onError = { error ->
                                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("claim_certificate_button")
                            ) {
                                Icon(Icons.Default.MilitaryTech, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Claim Cert", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
