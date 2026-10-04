package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.UhvViewModel

data class AiReviewItem(
    val id: String,
    val title: String,
    val category: String, // Lesson, Question, Reflection Feedback
    val generatedContent: String,
    val model: String = "Gemini 1.5 Pro / Server-side UHV",
    val promptVersion: String = "v2.4-academic-aicte",
    val generatedDate: String = "03 Oct 2026",
    var status: String = "PENDING_FACULTY_REVIEW"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAiReviewScreen(
    viewModel: UhvViewModel,
    language: Language
) {
    val context = LocalContext.current
    BackHandler { viewModel.navigateBack() }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Lessons (1)", "Questions (2)", "Study Plans (1)")

    var reviewItems by remember {
        mutableStateOf(
            listOf(
                AiReviewItem(
                    id = "rev_1",
                    title = "Holistic Technology Case Study in Nellore Region",
                    category = "Lesson",
                    generatedContent = "Analysis of traditional irrigation tanks (Cheruvulu) in Nellore and their alignment with the Pranic & Material orders, contrasted with modern unsustainable sand-mining practices."
                ),
                AiReviewItem(
                    id = "rev_2",
                    title = "Differentiating Sanyam from Bodily Suppression",
                    category = "Question",
                    generatedContent = "Question: 'Is intermittent fasting equivalent to Sanyam?' Options: A) Yes B) No, Sanyam is the conscious feeling of responsibility in 'I' for bodily health C) Only for monks D) Biological reflex."
                ),
                AiReviewItem(
                    id = "rev_3",
                    title = "Personalized 4-Week Reflection Roadmap for CSE 4th Year",
                    category = "Study Plan",
                    generatedContent = "Tailored study modules addressing placement stress, ethical AI development, and workplace trust for graduating engineers."
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AI Content Review Center",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Mandatory Faculty & Admin Academic Governance",
                            style = MaterialTheme.typography.labelSmall,
                            color = UhvGoldAccent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("ai_review_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .testTag("admin_ai_review_screen"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Governance Warning Card (Spec #12, #25)
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = UhvNavyPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = UhvGoldAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Institutional Governance Rule",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = UhvGoldLight
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Never automatically publish AI-generated academic content. Every artifact must transition from AI_GENERATED → FACULTY_REVIEW → APPROVED before students can view it.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Tab Row
            item {
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title, fontSize = 12.sp) }
                        )
                    }
                }
            }

            // Review Items List
            items(reviewItems) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = when (item.status) {
                                    "APPROVED" -> UhvSuccess.copy(alpha = 0.15f)
                                    "REJECTED" -> UhvError.copy(alpha = 0.15f)
                                    else -> UhvWarning.copy(alpha = 0.15f)
                                }
                            ) {
                                Text(
                                    text = item.status,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = when (item.status) {
                                        "APPROVED" -> UhvSuccess
                                        "REJECTED" -> UhvError
                                        else -> UhvWarning
                                    },
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.generatedContent,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Model: ${item.model} • Prompt: ${item.promptVersion} • Date: ${item.generatedDate}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    reviewItems = reviewItems.map {
                                        if (it.id == item.id) it.copy(status = "APPROVED") else it
                                    }
                                    Toast.makeText(context, "Content Approved & Published!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = UhvSuccess),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Approve", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    reviewItems = reviewItems.map {
                                        if (it.id == item.id) it.copy(status = "REJECTED") else it
                                    }
                                    Toast.makeText(context, "Content Rejected.", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = UhvError),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reject")
                            }
                        }
                    }
                }
            }
        }
    }
}
