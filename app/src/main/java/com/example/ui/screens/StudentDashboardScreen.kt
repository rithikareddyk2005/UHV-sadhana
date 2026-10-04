package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UhvViewModel

@Composable
fun StudentDashboardScreen(
    viewModel: UhvViewModel,
    currentUser: User?,
    language: Language,
    modules: List<UhvModule>,
    announcements: List<Announcement>,
    events: List<UhvEvent>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("student_dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Welcome Card with Official Logo & Background
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = UhvNavyPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_hero_card")
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Hero Page Background
                    Image(
                        painter = painterResource(id = R.drawable.uhv_hero_background_1791098863412),
                        contentDescription = "UHV Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    // Scrim gradient to ensure contrast and readability
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        UhvNavyDark.copy(alpha = 0.82f),
                                        UhvNavyPrimary.copy(alpha = 0.90f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == Language.EN) "Welcome to UHV Sadhana" else "యూహెచ్‌వి సాధనకు స్వాగతం",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = UhvGoldLight
                                )
                                Text(
                                    text = currentUser?.name ?: "Student",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = "Roll: ${currentUser?.rollNo.orEmpty()} • ${currentUser?.branch} - ${currentUser?.year} Year",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Official UHV Logo Seal
                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(2.dp, UhvGoldAccent),
                                shadowElevation = 6.dp,
                                modifier = Modifier.size(68.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.uhv_official_logo_1791098848144),
                                    contentDescription = "Universal Human Values Logo",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(3.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Department of Universal Human Values • NBKRIST",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "\"Service to Society, Commitment for Change\"",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = UhvGoldAccent
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Gamification Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatBadge(
                            label = "XP Points",
                            value = "${currentUser?.xp ?: 0}",
                            icon = Icons.Default.Bolt,
                            containerColor = Color.White.copy(alpha = 0.15f),
                            contentColor = UhvGoldLight
                        )
                        StatBadge(
                            label = "Daily Streak",
                            value = "${currentUser?.streakDays ?: 1} Days",
                            icon = Icons.Default.LocalFireDepartment,
                            containerColor = Color.White.copy(alpha = 0.15f),
                            contentColor = Color(0xFFFF9E80)
                        )
                        StatBadge(
                            label = "Modules",
                            value = "5 Enrolled",
                            icon = Icons.Default.MenuBook,
                            containerColor = Color.White.copy(alpha = 0.15f),
                            contentColor = Color(0xFFA7F3D0)
                        )
                    }
                }
            }
        }
    }

    // Quick Actions Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActionCard(
                        title = if (language == Language.EN) "Daily Reflections" else "రోజువారీ ఆలోచనలు",
                        subtitle = "Sentiment Journal",
                        icon = Icons.Default.EditNote,
                        color = Color(0xFF6B21A8),
                        modifier = Modifier.weight(1f).testTag("quick_action_daily_reflections"),
                        onClick = { viewModel.navigateTo(Screen.DAILY_REFLECTIONS) }
                    )
                    ActionCard(
                        title = if (language == Language.EN) "AI UHV Mentor" else "AI గురువు",
                        subtitle = "Ask UHV Doubts",
                        icon = Icons.Default.Psychology,
                        color = UhvNavyPrimary,
                        modifier = Modifier.weight(1f).testTag("quick_action_ai_mentor"),
                        onClick = { viewModel.navigateTo(Screen.AI_MENTOR) }
                    )
                }

                ActionCard(
                    title = if (language == Language.EN) "Public Certificate Verification" else "సర్టిఫికేట్ తనిఖీ",
                    subtitle = "/verify Portal • Anti-Fraud Credential Lookup",
                    icon = Icons.Default.Verified,
                    color = Color(0xFF0F766E),
                    modifier = Modifier.fillMaxWidth().testTag("quick_action_public_verify"),
                    onClick = { viewModel.navigateTo(Screen.PUBLIC_VERIFY) }
                )
            }
        }

        // Section Title: UHV Course Modules
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == Language.EN) "Universal Human Values Modules" else "సార్వత్రిక మానవ విలువల మాడ్యూల్స్",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "AICTE Syllabus",
                    style = MaterialTheme.typography.labelSmall,
                    color = UhvNavyPrimary
                )
            }
        }

        // List of 5 Modules
        items(modules) { module ->
            ModuleItemCard(
                module = module,
                language = language,
                onClick = { viewModel.openModule(module) }
            )
        }

        // Section: Announcements
        if (announcements.isNotEmpty()) {
            item {
                Text(
                    text = if (language == Language.EN) "Institutional Announcements" else "నోటీసులు & ప్రకటనలు",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(announcements) { ann ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (ann.isPinned) {
                                    Icon(
                                        imageVector = Icons.Default.PushPin,
                                        contentDescription = "Pinned",
                                        tint = UhvGoldAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = if (language == Language.EN) ann.titleEn else ann.titleTe,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = ann.datePosted,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = ann.content,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Section: Upcoming UHV Events
        if (events.isNotEmpty()) {
            item {
                Text(
                    text = if (language == Language.EN) "Upcoming Workshops & Events" else "రాబోయే వర్క్‌షాప్‌లు",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(events) { event ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (language == Language.EN) event.titleEn else event.titleTe,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp), tint = UhvNavyPrimary)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = event.eventDate, style = MaterialTheme.typography.labelSmall)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(14.dp), tint = UhvNavyPrimary)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = event.venue, style = MaterialTheme.typography.labelSmall)
                                }
                            }

                            if (event.isRegistered) {
                                AssistChip(
                                    onClick = { },
                                    label = { Text("Registered ✓", color = UhvSuccess) },
                                    leadingIcon = { Icon(Icons.Default.Check, contentDescription = null, tint = UhvSuccess) }
                                )
                            } else {
                                Button(
                                    onClick = { viewModel.registerForEvent(event.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = UhvGoldAccent),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text("Register", color = UhvNavyDark, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatBadge(
    label: String,
    value: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(text = value, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
fun ModuleItemCard(
    module: UhvModule,
    language: Language,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("module_card_${module.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                        text = "Module ${module.orderIndex}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = UhvNavyPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = "Unlocked",
                        tint = UhvSuccess,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Unlocked", style = MaterialTheme.typography.labelSmall, color = UhvSuccess)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == Language.EN) module.titleEn else module.titleTe,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (language == Language.EN) module.taglineEn else module.taglineTe,
                style = MaterialTheme.typography.bodySmall,
                color = UhvGoldAccent,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = module.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "${module.lecturesCount} Lectures",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${module.practiceCount} Practice Sessions",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FilledTonalButton(
                    onClick = onClick,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Explore", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}
