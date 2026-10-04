package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AuditLog
import com.example.data.model.UhvCertificate
import com.example.data.model.User
import com.example.ui.theme.*
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UhvViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    viewModel: UhvViewModel,
    students: List<User>,
    certificates: List<UhvCertificate>,
    auditLogs: List<AuditLog>,
    language: Language
) {
    BackHandler {
        // Switch back to Student Dashboard
        viewModel.navigateTo(Screen.STUDENT_DASHBOARD)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("admin_dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Header
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = UhvNavyPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "NBKRIST • UHV Cell Administration",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = UhvGoldLight
                            )
                            Text(
                                text = "Institutional Analytics & Control Portal",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = UhvGoldAccent,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        // Section: KPIs
        item {
            Text(
                text = "Key Institutional Metrics (KPIs)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "Registered Students",
                        value = "${students.size}",
                        subtitle = "Active in UHV Portal",
                        icon = Icons.Default.People,
                        color = UhvNavyPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Certificates Issued",
                        value = "${certificates.size}",
                        subtitle = "Verified Credentials",
                        icon = Icons.Default.MilitaryTech,
                        color = UhvGoldAccent,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "Quiz Pass Rate",
                        value = "84.5%",
                        subtitle = "Min 60% Passing",
                        icon = Icons.Default.CheckCircle,
                        color = UhvSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Avg Course Progress",
                        value = "68%",
                        subtitle = "Across 5 Modules",
                        icon = Icons.Default.Timeline,
                        color = Color(0xFF0284C7),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section: Data & Content Management Center
        item {
            Text(
                text = "Management Centers",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ManagementNavRow(
                    title = "Real Student Data Import Center",
                    description = "Import & validate college CSV/Excel registration records with duplicate detection",
                    icon = Icons.Default.UploadFile,
                    onClick = { viewModel.navigateTo(Screen.ADMIN_STUDENT_IMPORT) }
                )
                ManagementNavRow(
                    title = "Students Directory & Verification",
                    description = "Filter by Branch (CSE, ECE, EEE, MECH, CIVIL) and manage 'Needs Verification' queue",
                    icon = Icons.Default.ContactPage,
                    onClick = { viewModel.navigateTo(Screen.ADMIN_STUDENTS_DIRECTORY) }
                )
                ManagementNavRow(
                    title = "AI Content Review Center",
                    description = "Faculty/Admin governance for AI-assisted lessons, quiz questions, and study plans",
                    icon = Icons.Default.RateReview,
                    onClick = { viewModel.navigateTo(Screen.ADMIN_AI_REVIEW) }
                )
                ManagementNavRow(
                    title = "Institutional Audit Trail",
                    description = "Immutable log of logins, imports, certificate issuances, and role changes",
                    icon = Icons.Default.History,
                    onClick = { viewModel.navigateTo(Screen.ADMIN_AUDIT_LOGS) }
                )
            }
        }

        // Section: Recent Audit Feed
        item {
            Text(
                text = "Recent System Audit Events",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        items(auditLogs.take(5)) { log ->
            val dateStr = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(log.timestamp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = UhvNavyPrimary.copy(alpha = 0.1f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = UhvNavyPrimary, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${log.action} • ${log.actorRole}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = UhvNavyPrimary
                        )
                        Text(
                            text = log.target,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$dateStr by ${log.actorName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (log.result == "SUCCESS") UhvSuccess.copy(alpha = 0.15f) else UhvError.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = log.result,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (log.result == "SUCCESS") UhvSuccess else UhvError,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = UhvNavyDark)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
        }
    }
}

@Composable
fun ManagementNavRow(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = UhvNavyPrimary.copy(alpha = 0.1f),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = UhvNavyPrimary, modifier = Modifier.size(22.dp))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
