package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.UhvViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicVerifyScreen(
    viewModel: UhvViewModel,
    language: Language
) {
    BackHandler { viewModel.navigateBack() }

    val searchedId by viewModel.searchedCertId.collectAsState()
    val certResult by viewModel.verificationResult.collectAsState()
    val isVerifying by viewModel.isVerifying.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Certificate Verification (/verify)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Public Anti-Fraud Lookup Portal",
                            style = MaterialTheme.typography.labelSmall,
                            color = UhvGoldAccent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("verify_back_button")
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
                .testTag("public_verify_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Institutional Authority Banner
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
                                text = "NBKRIST Certificate Authority",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = UhvGoldLight
                            )
                            Icon(Icons.Default.Verified, contentDescription = null, tint = UhvGoldAccent)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Enter any official UHV Certificate ID (e.g., UHV-2026-7F3K9P2M) to verify academic credential authenticity.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Search Input Field
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = searchedId,
                            onValueChange = { viewModel.setSearchedCertId(it) },
                            label = { Text("Certificate ID") },
                            placeholder = { Text("e.g. UHV-2026-7F3K9P2M") },
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = UhvNavyPrimary)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("verify_search_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.verifyCertificate() },
                            enabled = !isVerifying && searchedId.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = UhvNavyPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("verify_search_button")
                        ) {
                            if (isVerifying) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                            } else {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Verify Authenticity", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Result Display
            if (certResult != null) {
                val cert = certResult!!
                val isValid = cert.status == "VALID"
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isValid) UhvSuccess.copy(alpha = 0.08f) else UhvError.copy(alpha = 0.08f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            if (isValid) UhvSuccess else UhvError
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("verify_result_card")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isValid) "STATUS: VALID ✓" else "STATUS: REVOKED ✗",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isValid) UhvSuccess else UhvError
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isValid) UhvSuccess.copy(alpha = 0.2f) else UhvError.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = cert.status,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isValid) UhvSuccess else UhvError,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(14.dp))

                            VerifyDetailRow(label = "Student Name", value = cert.studentName)
                            VerifyDetailRow(label = "Roll Number", value = cert.rollNo)
                            VerifyDetailRow(label = "Module", value = cert.moduleTitle)
                            VerifyDetailRow(label = "Issue Date", value = cert.issueDate)
                            VerifyDetailRow(label = "Certificate ID", value = cert.certificateId)
                            VerifyDetailRow(label = "Authority", value = cert.institutionalAuthority)
                            VerifyDetailRow(label = "Cryptographic Hash", value = cert.verificationHash)

                            if (!isValid && cert.revocationReason.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Revocation Reason: ${cert.revocationReason}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = UhvError
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Privacy Compliance Notice: Per institutional policy, student email, mobile numbers, and quiz answer sheets remain confidential and are never exposed publicly.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else if (searchedId.isNotBlank() && !isVerifying) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = UhvWarning,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Certificate Found",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "No record matched '$searchedId'. Please verify the identifier and try again.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerifyDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
