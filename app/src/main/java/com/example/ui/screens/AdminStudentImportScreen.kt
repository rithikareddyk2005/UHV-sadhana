package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminStudentImportScreen(
    viewModel: UhvViewModel,
    language: Language
) {
    val context = LocalContext.current
    BackHandler { viewModel.navigateBack() }

    val csvText by viewModel.importCsvText.collectAsState()
    val previewReport by viewModel.importPreview.collectAsState()
    val isImporting by viewModel.isImporting.collectAsState()
    val successMsg by viewModel.importSuccessMessage.collectAsState()

    var showRawEditor by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Real Student Data Import Center",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "NBKRIST Registration Data Pipeline",
                            style = MaterialTheme.typography.labelSmall,
                            color = UhvGoldAccent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("import_back_button")
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
                .testTag("admin_student_import_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pipeline Instructions Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UhvNavyPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Institutional Import Pipeline",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = UhvGoldLight
                            )
                            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = UhvGoldAccent)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Upload or paste official student records (.csv or .xlsx). The pipeline automatically normalizes branches (e.g. 'Computer Science and Engineering' → 'CSE'), detects duplicates, and routes incomplete records to the 'Needs Verification' queue.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Success Message Banner if any
            if (successMsg != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = UhvSuccess.copy(alpha = 0.15f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, UhvSuccess),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = UhvSuccess)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = successMsg!!,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = UhvSuccess
                            )
                        }
                    }
                }
            }

            // CSV Input Controls
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
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
                                text = "Source Data: nbkrist_student_registry.csv",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            TextButton(onClick = { showRawEditor = !showRawEditor }) {
                                Text(if (showRawEditor) "Collapse" else "View/Edit CSV")
                            }
                        }

                        if (showRawEditor) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = csvText,
                                onValueChange = { viewModel.setImportCsvText(it) },
                                minLines = 5,
                                maxLines = 10,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("csv_raw_text_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.parseAndPreviewCsv() },
                            colors = ButtonDefaults.buttonColors(containerColor = UhvNavyPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("validate_preview_button")
                        ) {
                            Icon(Icons.Default.ManageSearch, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Validate & Generate Preview", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Preview Report (Spec #9)
            if (previewReport != null) {
                val report = previewReport!!
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, UhvNavyPrimary.copy(alpha = 0.2f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("import_preview_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Data Import Validation Report",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = UhvNavyPrimary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ImportStatPill("Total Rows", "${report.totalRows}", UhvNavyPrimary, Modifier.weight(1f))
                                ImportStatPill("Valid", "${report.validRows}", UhvSuccess, Modifier.weight(1f))
                                ImportStatPill("Duplicates", "${report.duplicateRows}", UhvWarning, Modifier.weight(1f))
                                ImportStatPill("Flagged", "${report.needsVerificationRows}", UhvError, Modifier.weight(1f))
                            }

                            if (report.issuesSummary.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "Detected Validation Issues:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = UhvError
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                report.issuesSummary.forEach { issue ->
                                    Text(
                                        text = "• $issue",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Confirm Import Button
                            Button(
                                onClick = { viewModel.confirmImport() },
                                enabled = !isImporting && report.validRows > 0,
                                colors = ButtonDefaults.buttonColors(containerColor = UhvSuccess),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("confirm_import_button")
                            ) {
                                if (isImporting) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                                } else {
                                    Icon(Icons.Default.CloudDone, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Confirm & Commit ${report.validRows} Students to Database", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Table of Parsed Students Preview
                item {
                    Text(
                        text = "Parsed Records Preview (Normalized)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                items(report.parsedStudents) { student ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = student.fullName,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Roll: ${student.rollNo.ifEmpty { "MISSING" }} • ${student.branch} (Sec ${student.section}, Yr ${student.year})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (student.issues.isNotBlank()) {
                                    Text(
                                        text = student.issues,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (student.status == "DUPLICATE") UhvWarning else UhvError
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when (student.status) {
                                    "VALID" -> UhvSuccess.copy(alpha = 0.15f)
                                    "DUPLICATE" -> UhvWarning.copy(alpha = 0.15f)
                                    else -> UhvError.copy(alpha = 0.15f)
                                }
                            ) {
                                Text(
                                    text = student.status,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = when (student.status) {
                                        "VALID" -> UhvSuccess
                                        "DUPLICATE" -> UhvWarning
                                        else -> UhvError
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImportStatPill(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.1f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = color)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = color, fontSize = 9.sp)
        }
    }
}
