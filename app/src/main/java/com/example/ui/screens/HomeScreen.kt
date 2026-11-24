package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AllInclusive
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.LocalQuizSemanticColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    highScore: Int,
    onStartFullQuiz: () -> Unit,
    onStartEndlessDirectly: () -> Unit,
    contentPadding: PaddingValues,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val endlessColor = LocalQuizSemanticColors.current.endlessAccent
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    fun shareApp() {
        val shareText = "Check out Physics Pulse! Test yourself with 15 shuffled multi-topic physics questions and unlock Endless Calculation Mode."
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Physics Pulse — Unstoppable Physics Quiz")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Share Physics Pulse"))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .drawBehind {
                // Subtle orbital physics rings in the background
                drawCircle(
                    color = primaryColor.copy(alpha = 0.06f),
                    radius = size.width * 0.65f,
                    center = Offset(size.width * 0.9f, size.height * 0.12f),
                    style = Stroke(width = 2.dp.toPx()),
                )
                drawCircle(
                    color = endlessColor.copy(alpha = 0.05f),
                    radius = size.width * 0.45f,
                    center = Offset(size.width * 0.1f, size.height * 0.85f),
                    style = Stroke(width = 1.5.dp.toPx()),
                )
            }
            .padding(contentPadding),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Hero Banner Card with custom generated physics illustration
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                tonalElevation = 4.dp,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_physics_hero),
                        contentDescription = stringResource(R.string.hero_banner_desc),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x330B1120),
                                        Color(0xCC0B1120),
                                        Color(0xF50B1120),
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = primaryColor.copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, primaryColor.copy(alpha = 0.5f)),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Bolt,
                                        contentDescription = null,
                                        tint = primaryColor,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Text(
                                        text = "UNSTOPPABLE EDITION",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = primaryColor,
                                    )
                                }
                            }

                            if (highScore > 0) {
                                Surface(
                                    shape = CircleShape,
                                    color = endlessColor.copy(alpha = 0.18f),
                                    border = BorderStroke(1.dp, endlessColor.copy(alpha = 0.5f)),
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.EmojiEvents,
                                            contentDescription = null,
                                            tint = endlessColor,
                                            modifier = Modifier.size(14.dp),
                                        )
                                        Text(
                                            text = "BEST $highScore",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = endlessColor,
                                        )
                                    }
                                }
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Physics Pulse",
                                style = MaterialTheme.typography.displayLarge,
                                color = Color(0xFFF8FAFC),
                            )
                            Text(
                                text = "15 shuffled theory challenges followed by an endless calculation engine.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFCBD5E1),
                            )
                        }
                    }
                }
            }

            // Two-Phase Progression Cards
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "HOW IT WORKS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                PhaseOverviewCard(
                    phaseBadge = "PHASE 01",
                    title = "Multi-Topic Theory Gauntlet",
                    description = "15 randomized questions across 8 physics domains. Zero repeats until every card is answered.",
                    icon = Icons.Rounded.Quiz,
                    accentColor = primaryColor,
                )

                PhaseOverviewCard(
                    phaseBadge = "PHASE 02",
                    title = "Endless Calculation Mode",
                    description = "Clear all 15 theory questions to unlock infinite Speed, Force & Work math-physics problems.",
                    icon = Icons.Rounded.AllInclusive,
                    accentColor = endlessColor,
                )
            }

            // Covered Topics
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "TOPICS INCLUDED",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                val topics = listOf(
                    "Motion", "Force", "Energy", "Light",
                    "Sound", "Electricity", "Gravity", "Heat",
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    topics.forEach { topic ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        ) {
                            Text(
                                text = topic,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Primary CTAs
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onStartFullQuiz,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .testTag("start_quiz_button"),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.start_quiz),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }

                OutlinedButton(
                    onClick = onStartEndlessDirectly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("jump_endless_button"),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, endlessColor.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = endlessColor,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Calculate,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.jump_to_endless),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }

                TextButton(
                    onClick = ::shareApp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("share_app_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.share_app),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun PhaseOverviewCard(
    phaseBadge: String,
    title: String,
    description: String,
    icon: ImageVector,
    accentColor: Color,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = phaseBadge,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
