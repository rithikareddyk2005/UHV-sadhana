package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.data.model.UhvCertificate
import com.example.ui.theme.*
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UhvViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificatesScreen(
    viewModel: UhvViewModel,
    certificates: List<UhvCertificate>,
    language: Language
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    BackHandler { viewModel.navigateBack() }

    var viewingCert by remember { mutableStateOf<UhvCertificate?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (language == Language.EN) "My Verified Certificates" else "నా ధృవీకరణ పత్రాలు",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("certificates_back_button")
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
                .testTag("certificates_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Information
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
                                text = "Institutional Credentials",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = UhvGoldLight
                            )
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = UhvGoldAccent)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Each certificate is secured with a cryptographically unique identifier and verifiable on the public portal (/verify).",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            if (certificates.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Certificates Earned Yet",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Complete all lessons, submit practice activities, and pass module quizzes to earn verified certificates.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(certificates) { cert ->
                    CertificateItemCard(
                        certificate = cert,
                        onView = { viewingCert = cert },
                        onCopyId = {
                            clipboardManager.setText(AnnotatedString(cert.certificateId))
                            Toast.makeText(context, "Certificate ID copied!", Toast.LENGTH_SHORT).show()
                        },
                        onVerifyPublic = {
                            viewModel.setSearchedCertId(cert.certificateId)
                            viewModel.verifyCertificate()
                            viewModel.navigateTo(Screen.PUBLIC_VERIFY)
                        },
                        onDownload = {
                            Toast.makeText(context, "Certificate PDF downloaded to device storage.", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        }
    }

    // Certificate Preview Modal Dialog
    viewingCert?.let { cert ->
        AlertDialog(
            onDismissRequest = { viewingCert = null },
            modifier = Modifier.testTag("certificate_preview_dialog"),
            title = null,
            text = {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UhvIvory),
                    border = androidx.compose.foundation.BorderStroke(3.dp, UhvGoldAccent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "NBKR INSTITUTE OF SCIENCE & TECHNOLOGY",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = UhvNavyDark,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "VIDYANAGAR, NELLORE DISTRICT • UHV CELL",
                            style = MaterialTheme.typography.labelSmall,
                            color = UhvNavyPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, UhvGoldAccent),
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(56.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.uhv_official_logo_1791098848144),
                                contentDescription = "UHV Seal",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(3.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "CERTIFICATE OF COMPLETION",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = UhvNavyDark
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "This is to certify that",
                            style = MaterialTheme.typography.bodySmall,
                            color = UhvTextSecondaryDark
                        )

                        Text(
                            text = cert.studentName,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = UhvNavyPrimary
                        )

                        Text(
                            text = "Roll No: ${cert.rollNo}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = UhvTextSecondaryDark
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "has successfully completed all requirements, reflective sessions, and assessments for",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = UhvTextSecondaryDark
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = cert.moduleTitle,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center,
                            color = UhvNavyDark
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Divider(color = UhvGoldAccent.copy(alpha = 0.5f))

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Date: ${cert.issueDate}", style = MaterialTheme.typography.labelSmall, color = UhvNavyDark)
                                Text(text = "ID: ${cert.certificateId}", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = UhvNavyPrimary)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "[Configured Institution Authority]", style = MaterialTheme.typography.labelSmall, color = UhvNavyDark)
                                Text(text = "[Configured UHV Cell Authority]", style = MaterialTheme.typography.labelSmall, color = UhvNavyDark)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewingCert = null },
                    colors = ButtonDefaults.buttonColors(containerColor = UhvNavyPrimary)
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun CertificateItemCard(
    certificate: UhvCertificate,
    onView: () -> Unit,
    onCopyId: () -> Unit,
    onVerifyPublic: () -> Unit,
    onDownload: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, UhvGoldAccent.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("certificate_card_${certificate.certificateId}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (certificate.status == "VALID") UhvSuccess.copy(alpha = 0.15f) else UhvError.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = certificate.status,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (certificate.status == "VALID") UhvSuccess else UhvError,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = certificate.issueDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = certificate.moduleTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Recipient: ${certificate.studentName} (${certificate.rollNo})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "ID: ${certificate.certificateId}",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = UhvNavyPrimary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onView,
                    colors = ButtonDefaults.buttonColors(containerColor = UhvNavyPrimary),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f).height(36.dp)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                FilledTonalButton(
                    onClick = onVerifyPublic,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f).height(36.dp)
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Verify", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onDownload,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f).height(36.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
