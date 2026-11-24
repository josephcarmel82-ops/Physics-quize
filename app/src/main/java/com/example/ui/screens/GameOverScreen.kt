package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.AnsweredRecord
import com.example.model.QuizUiState
import com.example.ui.theme.LocalQuizSemanticColors

@Composable
fun GameOverScreen(
    uiState: QuizUiState,
    onPlayAgain: () -> Unit,
    onBackToHome: () -> Unit,
    contentPadding: PaddingValues,
) {
    val semantic = LocalQuizSemanticColors.current
    val context = LocalContext.current

    fun shareScore() {
        val summaryText = if (uiState.totalAnswered == 0) {
            "Test your physics skills with Physics Pulse — 15 shuffled theory challenges plus Endless Math Mode!"
        } else {
            "I scored ${uiState.score} / ${uiState.totalAnswered} (${uiState.accuracyPercent}% accuracy, ${uiState.bestStreak} best streak) on Physics Pulse! Can you clear all 15 theory questions and survive Endless Math Mode?"
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Physics Pulse Quiz Score")
            putExtra(Intent.EXTRA_TEXT, summaryText)
        }
        context.startActivity(Intent.createChooser(intent, "Share your Physics Pulse score"))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(contentPadding),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Final Score Hero Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                tonalElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(semantic.endlessContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.EmojiEvents,
                            contentDescription = "Final Score Trophy",
                            tint = semantic.endlessAccent,
                            modifier = Modifier.size(34.dp),
                        )
                    }

                    Text(
                        text = "GAME OVER",
                        style = MaterialTheme.typography.labelMedium,
                        color = semantic.endlessAccent,
                    )

                    Text(
                        text = "${uiState.score} / ${uiState.totalAnswered}",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.testTag("final_score_text"),
                    )

                    Text(
                        text = if (uiState.totalAnswered == 0) {
                            "No questions answered yet. Ready when you are!"
                        } else {
                            "Final Score across Theory & Endless Physics"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            // Stat Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatCard(
                    label = "ACCURACY",
                    value = "${uiState.accuracyPercent}%",
                    accent = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    label = "BEST STREAK",
                    value = "${uiState.bestStreak}",
                    accent = semantic.endlessAccent,
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    label = "MATH SOLVED",
                    value = "${uiState.endlessScore}/${uiState.endlessAttempted}",
                    accent = semantic.correct,
                    modifier = Modifier.weight(1f),
                )
            }

            // History Review List
            Text(
                text = "QUESTION BREAKDOWN",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (uiState.history.isEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Start a round to review your answers and physics formulas here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(uiState.history, key = { it.questionNumber }) { record ->
                        HistoryRowCard(record = record)
                    }
                }
            }

            // Bottom Actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedButton(
                    onClick = ::shareScore,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("share_score_button"),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, semantic.endlessAccent.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = semantic.endlessAccent,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.share_score),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedButton(
                        onClick = onBackToHome,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("home_button"),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Home,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.back_to_home),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }

                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("play_again_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.play_again),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = accent,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HistoryRowCard(record: AnsweredRecord) {
    val semantic = LocalQuizSemanticColors.current
    val statusColor = if (record.isCorrect) semantic.correct else semantic.wrong

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = if (record.isCorrect) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                contentDescription = if (record.isCorrect) "Correct" else "Wrong",
                tint = statusColor,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(20.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Q${record.questionNumber}",
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                    )
                    Text(
                        text = record.topic.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = record.prompt,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = if (record.isCorrect) {
                        "Answered: ${record.userAnswer}"
                    } else {
                        "Your answer: ${record.userAnswer}  •  Correct: ${record.correctAnswer}"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (record.isCorrect) semantic.correct else semantic.wrong,
                )
            }
        }
    }
}
