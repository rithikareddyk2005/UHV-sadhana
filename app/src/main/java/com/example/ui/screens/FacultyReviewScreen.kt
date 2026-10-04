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
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UhvViewModel

data class StudentReflectionReview(
    val id: String,
    val studentName: String,
    val rollNo: String,
    val branch: String,
    val activityTitle: String,
    val reflectionText: String,
    var feedback: String = "",
    var isApproved: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultyReviewScreen(
    viewModel: UhvViewModel,
    language: Language
) {
    val context = LocalContext.current
    BackHandler { viewModel.navigateTo(Screen.STUDENT_DASHBOARD) }

    var reviews by remember {
        mutableStateOf(
            listOf(
                StudentReflectionReview(
                    id = "ref_1",
                    studentName = "Sample Student",
                    rollNo = "25KB1A0000",
                    branch = "CSE",
                    activityTitle = "PS 1: Exploring Natural Acceptance vs Preconditioning",
                    reflectionText = "I examined my career choices and noticed I previously chose web development mainly because of peer salaries. After reflecting on natural acceptance, I realized my authentic fulfillment comes from building accessible educational tools for rural schools."
                ),
                StudentReflectionReview(
                    id = "ref_2",
                    studentName = "Rithika Reddy K",
                    rollNo = "23KB1A0589",
                    branch = "CSE",
                    activityTitle = "PS 4: Distinguishing Intention from Competence in Trust",
                    reflectionText = "During our engineering capstone team meetings, whenever someone arrived late, I assumed they were irresponsible. When I analyzed their intention, I realized they were traveling 40 km from Gudur on overcrowded buses. Assuming good intention completely cleared our friction."
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
                            text = "Faculty Academic Review",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Student Reflection & Assessment Assessment",
                            style = MaterialTheme.typography.labelSmall,
                            color = UhvGoldAccent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.STUDENT_DASHBOARD) },
                        modifier = Modifier.testTag("faculty_back_button")
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
                .testTag("faculty_review_screen"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = UhvNavyPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Faculty Review Guidelines",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = UhvGoldLight
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Universal Human Values reflections are personal journeys of self-exploration. Grade or review with compassion and encouragement, focusing on honest introspection rather than academic jargon.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            items(reviews) { item ->
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
                            Column {
                                Text(
                                    text = item.studentName,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Roll: ${item.rollNo} • ${item.branch}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (item.isApproved) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = UhvSuccess.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "Reviewed ✓",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = UhvSuccess,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.activityTitle,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = UhvNavyPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "\"${item.reflectionText}\"",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    reviews = reviews.map {
                                        if (it.id == item.id) it.copy(isApproved = true) else it
                                    }
                                    Toast.makeText(context, "Reflection marked as approved & verified.", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = UhvSuccess),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Approve", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
