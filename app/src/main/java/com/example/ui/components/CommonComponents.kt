package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.ui.theme.*
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UhvViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UhvTopAppBar(
    viewModel: UhvViewModel,
    currentUser: User?,
    language: Language,
    isDemoMode: Boolean,
    onRoleSwitchClick: () -> Unit
) {
    Surface(
        color = UhvNavyPrimary,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth().testTag("uhv_top_app_bar")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // College Branding & Official Logo
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, UhvGoldAccent),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.uhv_official_logo_1791098848144),
                            contentDescription = "UHV Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "NBKRIST • UHV SADHANA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (language == Language.EN)
                                "\"Existence is Co-existence\""
                            else
                                "\"సహ-అస్తిత్వమే ఉనికి\"",
                            style = MaterialTheme.typography.labelSmall,
                            color = UhvGoldAccent
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Demo Mode / Prod Badge
                    Surface(
                        color = if (isDemoMode) UhvWarning.copy(alpha = 0.2f) else UhvSuccess.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { viewModel.toggleDemoMode() }
                    ) {
                        Text(
                            text = if (isDemoMode) "DEMO" else "PROD",
                            color = if (isDemoMode) UhvGoldLight else UhvSuccess,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Language Toggle
                    OutlinedButton(
                        onClick = { viewModel.toggleLanguage() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(UhvGoldAccent)
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp).testTag("language_toggle_button")
                    ) {
                        Text(
                            text = if (language == Language.EN) "తెలుగు" else "ENG",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Role Switcher / Profile Button
                    Surface(
                        color = UhvGoldAccent,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .clickable { onRoleSwitchClick() }
                            .testTag("role_switcher_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = when (currentUser?.role) {
                                    UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                    UserRole.SUPER_ADMIN -> Icons.Default.Security
                                    UserRole.FACULTY -> Icons.Default.School
                                    else -> Icons.Default.Person
                                },
                                contentDescription = "Role",
                                tint = UhvNavyDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentUser?.role?.name?.take(7) ?: "STUDENT",
                                color = UhvNavyDark,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UhvBottomNavigationBar(
    currentScreen: Screen,
    userRole: UserRole,
    onNavigate: (Screen) -> Unit
) {
    val isAdmin = userRole == UserRole.ADMIN || userRole == UserRole.SUPER_ADMIN

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        if (isAdmin) {
            NavigationBarItem(
                selected = currentScreen == Screen.ADMIN_DASHBOARD,
                onClick = { onNavigate(Screen.ADMIN_DASHBOARD) },
                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                label = { Text("Overview", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_admin_overview")
            )
            NavigationBarItem(
                selected = currentScreen == Screen.ADMIN_STUDENT_IMPORT,
                onClick = { onNavigate(Screen.ADMIN_STUDENT_IMPORT) },
                icon = { Icon(Icons.Default.UploadFile, contentDescription = "Import Center") },
                label = { Text("Import", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_admin_import")
            )
            NavigationBarItem(
                selected = currentScreen == Screen.ADMIN_STUDENTS_DIRECTORY,
                onClick = { onNavigate(Screen.ADMIN_STUDENTS_DIRECTORY) },
                icon = { Icon(Icons.Default.People, contentDescription = "Students") },
                label = { Text("Students", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_admin_students")
            )
            NavigationBarItem(
                selected = currentScreen == Screen.ADMIN_AI_REVIEW,
                onClick = { onNavigate(Screen.ADMIN_AI_REVIEW) },
                icon = { Icon(Icons.Default.RateReview, contentDescription = "AI Review") },
                label = { Text("AI Review", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_admin_ai_review")
            )
            NavigationBarItem(
                selected = currentScreen == Screen.ADMIN_AUDIT_LOGS,
                onClick = { onNavigate(Screen.ADMIN_AUDIT_LOGS) },
                icon = { Icon(Icons.Default.History, contentDescription = "Audit Logs") },
                label = { Text("Audit", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_admin_audit")
            )
        } else {
            NavigationBarItem(
                selected = currentScreen == Screen.STUDENT_DASHBOARD,
                onClick = { onNavigate(Screen.STUDENT_DASHBOARD) },
                icon = { Icon(Icons.Default.School, contentDescription = "Learn") },
                label = { Text("Courses", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_courses")
            )
            NavigationBarItem(
                selected = currentScreen == Screen.DAILY_REFLECTIONS,
                onClick = { onNavigate(Screen.DAILY_REFLECTIONS) },
                icon = { Icon(Icons.Default.EditNote, contentDescription = "Daily Reflections") },
                label = { Text("Reflections", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_reflections")
            )
            NavigationBarItem(
                selected = currentScreen == Screen.AI_MENTOR,
                onClick = { onNavigate(Screen.AI_MENTOR) },
                icon = { Icon(Icons.Default.Psychology, contentDescription = "AI Mentor") },
                label = { Text("AI Mentor", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_ai_mentor")
            )
            NavigationBarItem(
                selected = currentScreen == Screen.CERTIFICATES,
                onClick = { onNavigate(Screen.CERTIFICATES) },
                icon = { Icon(Icons.Default.MilitaryTech, contentDescription = "Certificates") },
                label = { Text("Certificates", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_certificates")
            )
            NavigationBarItem(
                selected = currentScreen == Screen.LEADERBOARD,
                onClick = { onNavigate(Screen.LEADERBOARD) },
                icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Leaderboard") },
                label = { Text("Rank", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_leaderboard")
            )
        }
    }
}

@Composable
fun RoleSwitcherDialog(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Demo Role Switcher",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Instant access to all master specification user roles",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                RoleOptionItem(
                    role = UserRole.STUDENT,
                    title = "Demo Student",
                    subtitle = "Sample Student (25KB1A0000) • CSE IV",
                    credentials = "student@uhvsadhana.demo",
                    isSelected = currentRole == UserRole.STUDENT,
                    onClick = { onRoleSelected(UserRole.STUDENT); onDismiss() }
                )
                RoleOptionItem(
                    role = UserRole.FACULTY,
                    title = "Demo Faculty",
                    subtitle = "Dr. K. S. Murthy • UHV Cell",
                    credentials = "faculty@uhvsadhana.demo",
                    isSelected = currentRole == UserRole.FACULTY,
                    onClick = { onRoleSelected(UserRole.FACULTY); onDismiss() }
                )
                RoleOptionItem(
                    role = UserRole.ADMIN,
                    title = "Demo Admin",
                    subtitle = "UHV Cell Coordinator • Student Imports & Content",
                    credentials = "admin@uhvsadhana.demo",
                    isSelected = currentRole == UserRole.ADMIN,
                    onClick = { onRoleSelected(UserRole.ADMIN); onDismiss() }
                )
                RoleOptionItem(
                    role = UserRole.SUPER_ADMIN,
                    title = "Demo Super Admin",
                    subtitle = "Principal / Director • Full System Control",
                    credentials = "superadmin@uhvsadhana.demo",
                    isSelected = currentRole == UserRole.SUPER_ADMIN,
                    onClick = { onRoleSelected(UserRole.SUPER_ADMIN); onDismiss() }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun RoleOptionItem(
    role: UserRole,
    title: String,
    subtitle: String,
    credentials: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, UhvGoldAccent) else null,
        modifier = Modifier.fillMaxWidth().testTag("role_option_${role.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = UhvNavyPrimary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = credentials,
                    style = MaterialTheme.typography.labelSmall,
                    color = UhvNavyPrimary
                )
            }
        }
    }
}
