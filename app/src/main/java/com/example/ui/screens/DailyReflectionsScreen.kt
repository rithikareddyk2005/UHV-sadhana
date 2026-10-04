package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DailyUhvReflection
import com.example.data.model.SentimentCategory
import com.example.data.model.User
import com.example.ui.theme.*
import com.example.ui.viewmodel.DailyReflectionViewModel
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.UhvViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyReflectionsScreen(
    uhvViewModel: UhvViewModel,
    reflectionViewModel: DailyReflectionViewModel,
    currentUser: User?,
    language: Language
) {
    val context = LocalContext.current
    BackHandler { uhvViewModel.navigateBack() }

    LaunchedEffect(currentUser?.id) {
        if (currentUser != null) {
            reflectionViewModel.setUserId(currentUser.id)
        }
    }

    val reflections by reflectionViewModel.filteredReflections.collectAsState()
    val sentimentCounts by reflectionViewModel.sentimentCounts.collectAsState()
    val selectedFilter by reflectionViewModel.selectedSentimentFilter.collectAsState()

    val title by reflectionViewModel.title.collectAsState()
    val reflectionText by reflectionViewModel.reflectionText.collectAsState()
    val selectedTheme by reflectionViewModel.selectedTheme.collectAsState()
    val selectedSentiment by reflectionViewModel.selectedSentiment.collectAsState()
    val moodRating by reflectionViewModel.moodRating.collectAsState()
    val isSaving by reflectionViewModel.isSaving.collectAsState()

    var showNewEntryDialog by remember { mutableStateOf(false) }

    val uhvThemes = listOf(
        "Harmony in the Self",
        "Trust in Relationships",
        "Respect & Justice",
        "Harmony in Nature",
        "Professional Ethics"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (language == Language.EN) "Daily UHV Reflections" else "రోజువారీ సాధన ఆలోచనలు",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Self-Exploration & Sentiment Journal",
                            style = MaterialTheme.typography.labelSmall,
                            color = UhvGoldAccent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { uhvViewModel.navigateBack() },
                        modifier = Modifier.testTag("reflection_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showNewEntryDialog = true },
                containerColor = UhvNavyPrimary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Reflection") },
                text = { Text("New Reflection", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("new_reflection_fab")
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .testTag("daily_reflections_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Banner with Hero Background & Official Logo
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = UhvNavyPrimary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reflection_header_card")
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Hero Page Background
                        Image(
                            painter = painterResource(id = R.drawable.uhv_hero_background_1791098863412),
                            contentDescription = "UHV Background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )

                        // Dark gradient scrim
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            UhvNavyDark.copy(alpha = 0.85f),
                                            UhvNavyPrimary.copy(alpha = 0.90f)
                                        )
                                    )
                                )
                        )

                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (language == Language.EN) "Daily Sadhana Reflection Journal" else "రోజువారీ సాధన ఆలోచనలు",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = UhvGoldLight
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Record reflections with timestamp & sentiment categorization",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.88f)
                                    )
                                    Text(
                                        text = "Universal Human Values • NBKRIST",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = UhvGoldAccent
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Surface(
                                    shape = CircleShape,
                                    color = Color.White,
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, UhvGoldAccent),
                                    shadowElevation = 4.dp,
                                    modifier = Modifier.size(54.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.uhv_official_logo_1791098848144),
                                        contentDescription = "UHV Official Logo",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(3.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Sentiment Categorization Filter Row
            item {
                Text(
                    text = "Filter by Sentiment Category:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedFilter == null,
                            onClick = { reflectionViewModel.setSentimentFilter(null) },
                            label = { Text("All (${reflections.size})", fontSize = 11.sp) }
                        )
                    }

                    items(SentimentCategory.entries) { cat ->
                        val count = sentimentCounts[cat] ?: 0
                        FilterChip(
                            selected = selectedFilter == cat,
                            onClick = { reflectionViewModel.setSentimentFilter(cat) },
                            label = { Text("${cat.emoji} ${cat.displayName.take(15)} ($count)", fontSize = 11.sp) }
                        )
                    }
                }
            }

            // Reflections List
            if (reflections.isEmpty()) {
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
                                Icons.Default.EditNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(52.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No Daily Reflections Yet",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Tap '+ New Reflection' to record your observations on trust, relationships, self, or nature.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(reflections) { reflection ->
                    ReflectionCard(
                        reflection = reflection,
                        onDelete = {
                            reflectionViewModel.deleteReflection(reflection.id)
                            Toast.makeText(context, "Reflection deleted", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }

    // New Reflection Dialog Modal
    if (showNewEntryDialog) {
        AlertDialog(
            onDismissRequest = { showNewEntryDialog = false },
            title = {
                Text(
                    text = "Record Daily Reflection",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { reflectionViewModel.setTitle(it) },
                            label = { Text("Reflection Title") },
                            placeholder = { Text("e.g. Practicing Trust in Lab Team") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reflection_title_input")
                        )
                    }

                    item {
                        Text(
                            text = "UHV Theme:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(uhvThemes) { theme ->
                                FilterChip(
                                    selected = selectedTheme == theme,
                                    onClick = { reflectionViewModel.setSelectedTheme(theme) },
                                    label = { Text(theme, fontSize = 10.sp) }
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Sentiment Categorization:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            SentimentCategory.entries.forEach { cat ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (selectedSentiment == cat) UhvNavyPrimary.copy(alpha = 0.1f) else Color.Transparent,
                                    border = if (selectedSentiment == cat) androidx.compose.foundation.BorderStroke(1.5.dp, UhvNavyPrimary) else null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { reflectionViewModel.setSelectedSentiment(cat) }
                                        .padding(vertical = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selectedSentiment == cat,
                                            onClick = { reflectionViewModel.setSelectedSentiment(cat) },
                                            colors = RadioButtonDefaults.colors(selectedColor = UhvNavyPrimary)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "${cat.emoji} ${cat.displayName}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Inner Harmony Rating (1-5):",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            (1..5).forEach { star ->
                                IconButton(
                                    onClick = { reflectionViewModel.setMoodRating(star) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (star <= moodRating) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "$star Stars",
                                        tint = if (star <= moodRating) UhvGoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = reflectionText,
                            onValueChange = { reflectionViewModel.setReflectionText(it) },
                            label = { Text("Observation & Reflection") },
                            placeholder = { Text("What did you observe in yourself or relationships today? What does Natural Acceptance say?") },
                            minLines = 4,
                            maxLines = 8,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reflection_body_input")
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val user = currentUser ?: return@Button
                        reflectionViewModel.saveReflection(user.id, user.name) {
                            Toast.makeText(context, "Reflection recorded! +20 XP awarded.", Toast.LENGTH_SHORT).show()
                            showNewEntryDialog = false
                        }
                    },
                    enabled = !isSaving && title.isNotBlank() && reflectionText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = UhvNavyPrimary),
                    modifier = Modifier.testTag("save_reflection_button")
                ) {
                    Text("Save Reflection (+20 XP)", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewEntryDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ReflectionCard(
    reflection: DailyUhvReflection,
    onDelete: () -> Unit
) {
    val dateStr = if (reflection.dateFormatted.isNotBlank()) {
        reflection.dateFormatted
    } else {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(reflection.timestamp))
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("reflection_card_${reflection.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sentiment Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (reflection.sentiment) {
                        SentimentCategory.HARMONIOUS -> Color(0xFFD1FAE5)
                        SentimentCategory.GRATITUDE -> Color(0xFFFEF3C7)
                        SentimentCategory.INSIGHTFUL -> Color(0xFFE0E7FF)
                        SentimentCategory.CONTEMPLATIVE -> Color(0xFFF3E8FF)
                        SentimentCategory.CHALLENGED -> Color(0xFFFFEDD5)
                    }
                ) {
                    Text(
                        text = "${reflection.sentiment.emoji} ${reflection.sentiment.displayName}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = when (reflection.sentiment) {
                            SentimentCategory.HARMONIOUS -> Color(0xFF065F46)
                            SentimentCategory.GRATITUDE -> Color(0xFF92400E)
                            SentimentCategory.INSIGHTFUL -> Color(0xFF3730A3)
                            SentimentCategory.CONTEMPLATIVE -> Color(0xFF6B21A8)
                            SentimentCategory.CHALLENGED -> Color(0xFF9A3412)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Delete Action
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reflection.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Theme: ${reflection.uhvTheme}",
                    style = MaterialTheme.typography.labelSmall,
                    color = UhvNavyPrimary
                )
                Text(
                    text = " • ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Harmony Stars
                Row {
                    (1..reflection.moodRating).forEach {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = UhvGoldAccent,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reflection.reflectionText,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "Author: ${reflection.userName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}
