package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.data.model.QuizQuestion
import com.example.data.model.UhvModule
import com.example.ui.theme.*
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UhvViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: UhvViewModel,
    module: UhvModule,
    questions: List<QuizQuestion>,
    userAnswers: Map<String, Int>,
    language: Language
) {
    val context = LocalContext.current
    BackHandler { viewModel.navigateBack() }

    val lastResult by viewModel.lastQuizResult.collectAsState()
    val isSubmitting by viewModel.isSubmittingQuiz.collectAsState()

    var timeRemainingSeconds by remember { mutableIntStateOf(1800) } // 30 minutes

    LaunchedEffect(Unit) {
        while (timeRemainingSeconds > 0 && lastResult == null) {
            kotlinx.coroutines.delay(1000)
            timeRemainingSeconds--
        }
    }

    val minutes = timeRemainingSeconds / 60
    val seconds = timeRemainingSeconds % 60
    val timerString = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Module Assessment",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Server-Authoritative Evaluation Engine",
                            style = MaterialTheme.typography.labelSmall,
                            color = UhvGoldAccent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("quiz_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = UhvNavyDark,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = null,
                                tint = UhvGoldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = timerString,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .testTag("quiz_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Result Card if completed
            if (lastResult != null) {
                val res = lastResult!!
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (res.passed) UhvSuccess.copy(alpha = 0.12f) else UhvError.copy(alpha = 0.12f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            if (res.passed) UhvSuccess else UhvError
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quiz_result_card")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (res.passed) "ASSESSMENT PASSED ✓" else "NEEDS RETAKE",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (res.passed) UhvSuccess else UhvError
                                )
                                Text(
                                    text = "${res.correctCount}/${res.totalQuestions} (${String.format("%.1f", res.percentage)}%)",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (res.passed) UhvSuccess else UhvError
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = res.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (res.passed) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        viewModel.requestCertificateForCurrentModule(
                                            onSuccess = { cert ->
                                                Toast.makeText(context, "Certificate Issued: ${cert.certificateId}", Toast.LENGTH_LONG).show()
                                                viewModel.navigateTo(Screen.CERTIFICATES)
                                            },
                                            onError = { err ->
                                                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = UhvNavyPrimary),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("quiz_claim_cert_button")
                                ) {
                                    Icon(Icons.Default.MilitaryTech, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Claim Official Certificate", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Quiz Instructions Banner
            if (lastResult == null) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = UhvNavyPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Instructions & Security Rules",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = UhvGoldLight
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Answer all questions before submitting.\n• Browser does not calculate scores; evaluation occurs through authoritative logic.\n• Passing benchmark: 60%. Maximum 3 attempts allowed.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            // Question List
            itemsIndexed(questions) { index, question ->
                val selectedOption = userAnswers[question.id]
                val options = question.optionsJson.split("|")

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("question_card_$index")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Question ${index + 1} of ${questions.size}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = UhvNavyPrimary
                            )
                            Text(
                                text = "Difficulty: ${question.difficulty}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (language == Language.EN) question.questionEn else question.questionTe,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Options
                        options.forEachIndexed { optIndex, optionText ->
                            val isSelected = selectedOption == optIndex
                            val isCorrectAnswer = optIndex == question.correctOptionIndex
                            val isSubmitted = lastResult != null

                            val optionBorderColor = when {
                                isSubmitted && isCorrectAnswer -> UhvSuccess
                                isSubmitted && isSelected && !isCorrectAnswer -> UhvError
                                isSelected -> UhvNavyPrimary
                                else -> MaterialTheme.colorScheme.outlineVariant
                            }

                            val optionContainerColor = when {
                                isSubmitted && isCorrectAnswer -> UhvSuccess.copy(alpha = 0.1f)
                                isSubmitted && isSelected && !isCorrectAnswer -> UhvError.copy(alpha = 0.1f)
                                isSelected -> UhvNavyPrimary.copy(alpha = 0.08f)
                                else -> MaterialTheme.colorScheme.surface
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = optionContainerColor,
                                border = androidx.compose.foundation.BorderStroke(1.dp, optionBorderColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable(enabled = lastResult == null) {
                                        viewModel.selectQuizAnswer(question.id, optIndex)
                                    }
                                    .testTag("option_${index}_$optIndex")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            if (lastResult == null) {
                                                viewModel.selectQuizAnswer(question.id, optIndex)
                                            }
                                        },
                                        colors = RadioButtonDefaults.colors(selectedColor = UhvNavyPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = optionText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Explanation if submitted
                        if (lastResult != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = UhvNavyPrimary.copy(alpha = 0.08f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "UHV Concept Explanation:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = UhvNavyPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = question.explanation,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Submit Button
            if (lastResult == null) {
                item {
                    val answeredCount = userAnswers.size
                    val allAnswered = answeredCount == questions.size

                    Button(
                        onClick = {
                            val timeSpent = 1800 - timeRemainingSeconds
                            viewModel.submitQuiz(timeSpent)
                        },
                        enabled = !isSubmitting && userAnswers.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = UhvNavyPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_quiz_button")
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Submit Answers for Evaluation ($answeredCount/${questions.size})",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
