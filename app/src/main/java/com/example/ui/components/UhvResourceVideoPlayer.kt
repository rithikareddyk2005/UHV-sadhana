package com.example.ui.components

import android.net.Uri
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.R
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun UhvResourceVideoPlayer(
    rawResourceId: Int = R.raw.uhv_lecture_1,
    title: String = "AICTE UHV Video Lecture",
    onWatchProgressUpdated: (Float) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isPlaying by remember { mutableStateOf(false) }
    var currentPositionMs by remember { mutableIntStateOf(0) }
    var durationMs by remember { mutableIntStateOf(1) }
    var isCompleted by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var isPrepared by remember { mutableStateOf(false) }

    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }

    val videoUri = remember(rawResourceId) {
        Uri.parse("android.resource://${context.packageName}/$rawResourceId")
    }

    // Monitor playback position and update progress
    LaunchedEffect(isPlaying, isPrepared) {
        while (isPlaying && !hasError) {
            videoViewRef?.let { vv ->
                runCatching {
                    if (vv.isPlaying) {
                        val pos = vv.currentPosition
                        val dur = vv.duration.coerceAtLeast(1)
                        currentPositionMs = pos
                        durationMs = dur
                        val progressRatio = pos.toFloat() / dur.toFloat()
                        onWatchProgressUpdated(progressRatio)
                    }
                }
            }
            delay(500)
        }
    }

    // Auto-hide controls after 3.5s of playing
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            delay(3500)
            showControls = false
        }
    }

    // Lifecycle observer to pause video when app goes to background
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    runCatching { videoViewRef?.pause() }
                    isPlaying = false
                }
                Lifecycle.Event.ON_DESTROY -> {
                    runCatching { videoViewRef?.stopPlayback() }
                    videoViewRef = null
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            runCatching { videoViewRef?.stopPlayback() }
            videoViewRef = null
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UhvNavyDark),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("resource_video_player_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = UhvGoldAccent.copy(alpha = 0.2f),
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = UhvGoldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Playing from App Resources",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = UhvGoldLight
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "HD 720p • Direct Raw",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Video Player Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
                    .clickable { showControls = !showControls }
                    .testTag("video_player_surface"),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT
                            )
                            setVideoURI(videoUri)
                            setOnPreparedListener { mp ->
                                isPrepared = true
                                durationMs = mp.duration.coerceAtLeast(1)
                                mp.isLooping = false
                                runCatching {
                                    start()
                                    isPlaying = true
                                }
                            }
                            setOnCompletionListener {
                                isPlaying = false
                                isCompleted = true
                                showControls = true
                                currentPositionMs = durationMs
                                onWatchProgressUpdated(1.0f)
                            }
                            setOnErrorListener { _, _, _ ->
                                hasError = true
                                isPlaying = false
                                true
                            }
                            videoViewRef = this
                        }
                    },
                    update = { vv ->
                        videoViewRef = vv
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay Controls
                if (showControls || !isPlaying) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (hasError) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = UhvError, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Unable to play video", color = Color.White, style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(6.dp))
                                Button(
                                    onClick = {
                                        hasError = false
                                        videoViewRef?.let { vv ->
                                            runCatching {
                                                vv.setVideoURI(videoUri)
                                                vv.start()
                                                isPlaying = true
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = UhvGoldAccent)
                                ) {
                                    Text("Retry", color = UhvNavyDark)
                                }
                            }
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Rewind 5s
                                IconButton(
                                    onClick = {
                                        videoViewRef?.let { vv ->
                                            runCatching {
                                                val target = (vv.currentPosition - 5000).coerceAtLeast(0)
                                                vv.seekTo(target)
                                                currentPositionMs = target
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(Icons.Default.Replay5, contentDescription = "Rewind 5s", tint = Color.White)
                                }

                                // Play / Pause Button
                                IconButton(
                                    onClick = {
                                        videoViewRef?.let { vv ->
                                            runCatching {
                                                if (vv.isPlaying) {
                                                    vv.pause()
                                                    isPlaying = false
                                                } else {
                                                    if (isCompleted) {
                                                        vv.seekTo(0)
                                                        isCompleted = false
                                                    }
                                                    vv.start()
                                                    isPlaying = true
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(UhvGoldAccent, CircleShape)
                                        .testTag("video_play_pause_button")
                                ) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlaying) "Pause" else "Play",
                                        tint = UhvNavyDark,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                // Forward 5s
                                IconButton(
                                    onClick = {
                                        videoViewRef?.let { vv ->
                                            runCatching {
                                                val target = (vv.currentPosition + 5000).coerceAtMost(durationMs)
                                                vv.seekTo(target)
                                                currentPositionMs = target
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(Icons.Default.Forward5, contentDescription = "Forward 5s", tint = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Playback Timeline Slider & Time Labels
            val currentSec = currentPositionMs / 1000
            val totalSec = durationMs / 1000
            val timeString = String.format("%02d:%02d / %02d:%02d", currentSec / 60, currentSec % 60, totalSec / 60, totalSec % 60)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f)
                )

                val watchPercentage = ((currentPositionMs.toFloat() / durationMs.toFloat()) * 100f).toInt().coerceIn(0, 100)
                Text(
                    text = if (watchPercentage >= 80) "80% Watch Rule Satisfied ✓" else "$watchPercentage% watched (80% required)",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (watchPercentage >= 80) UhvSuccess else UhvWarning
                )
            }

            Slider(
                value = currentPositionMs.toFloat().coerceIn(0f, durationMs.toFloat()),
                onValueChange = { newPos ->
                    currentPositionMs = newPos.toInt()
                    videoViewRef?.seekTo(newPos.toInt())
                },
                valueRange = 0f..durationMs.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = UhvGoldAccent,
                    activeTrackColor = UhvGoldAccent,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .testTag("video_seek_slider")
            )
        }
    }
}
