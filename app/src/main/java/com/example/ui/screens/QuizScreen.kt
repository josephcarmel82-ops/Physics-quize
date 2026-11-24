package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.AllInclusive
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AnswerFeedback
import com.example.model.QuizOption
import com.example.model.QuizPhase
import com.example.model.QuizQuestion.CalculationQuestion
import com.example.model.QuizQuestion.TheoryQuestion
import com.example.model.QuizUiState
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.LocalQuizSemanticColors

@Composable
fun QuizScreen(
    uiState: QuizUiState,
    onSelectTheoryOption: (String) -> Unit,
    onAppendDigit: (Char) -> Unit,
    onBackspaceDigit: () -> Unit,
    onClearDigits: () -> Unit,
    onSubmitCalculation: () -> Unit,
    onNextQuestion: () -> Unit,
    onQuitQuiz: () -> Unit,
    contentPadding: PaddingValues,
) {
    val isEndless = uiState.phase == QuizPhase.Endless
    val semanticColors = LocalQuizSemanticColors.current
    val modeAccent = if (isEndless) semanticColors.endlessAccent else MaterialTheme.colorScheme.primary
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(contentPadding)
            .onPreviewKeyEvent { keyEvent ->
                if (!isEndless || uiState.feedback != null || keyEvent.type != KeyEventType.KeyDown) {
                    return@onPreviewKeyEvent false
                }
                when (keyEvent.key) {
                    Key.Zero, Key.NumPad0 -> { onAppendDigit('0'); true }
                    Key.One, Key.NumPad1 -> { onAppendDigit('1'); true }
                    Key.Two, Key.NumPad2 -> { onAppendDigit('2'); true }
                    Key.Three, Key.NumPad3 -> { onAppendDigit('3'); true }
                    Key.Four, Key.NumPad4 -> { onAppendDigit('4'); true }
                    Key.Five, Key.NumPad5 -> { onAppendDigit('5'); true }
                    Key.Six, Key.NumPad6 -> { onAppendDigit('6'); true }
                    Key.Seven, Key.NumPad7 -> { onAppendDigit('7'); true }
                    Key.Eight, Key.NumPad8 -> { onAppendDigit('8'); true }
                    Key.Nine, Key.NumPad9 -> { onAppendDigit('9'); true }
                    Key.Backspace -> { onBackspaceDigit(); true }
                    Key.Enter, Key.NumPadEnter -> { onSubmitCalculation(); true }
                    else -> false
                }
            }
            .focusable(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Top Quiz HUD
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Phase Badge
                Surface(
                    shape = CircleShape,
                    color = modeAccent.copy(alpha = 0.14f),
                    border = BorderStroke(1.dp, modeAccent.copy(alpha = 0.45f)),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(
                            imageVector = if (isEndless) Icons.Rounded.AllInclusive else Icons.Rounded.Science,
                            contentDescription = null,
                            tint = modeAccent,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = if (isEndless) "ENDLESS MATH" else "THEORY ${uiState.theoryIndex + 1}/${uiState.theoryTotal}",
                            style = MaterialTheme.typography.labelSmall,
                            color = modeAccent,
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (uiState.currentStreak >= 2) {
                        Surface(
                            shape = CircleShape,
                            color = semanticColors.endlessContainer,
                            border = BorderStroke(1.dp, semanticColors.endlessAccent.copy(alpha = 0.5f)),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = semanticColors.endlessAccent,
                                    modifier = Modifier.size(14.dp),
                                )
                                Text(
                                    text = "${uiState.currentStreak}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = semanticColors.endlessAccent,
                                )
                            }
                        }
                    }

                    // Score Chip
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.testTag("live_score_badge"),
                    ) {
                        Text(
                            text = "SCORE ${uiState.score}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        )
                    }

                    // Quit button
                    TextButton(
                        onClick = onQuitQuiz,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("quit_quiz_button"),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.quit_quiz),
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.quit_quiz),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }

            // Progress Bar
            if (!isEndless) {
                val targetProgress = (uiState.theoryIndex + if (uiState.feedback != null) 1 else 0) /
                    uiState.theoryTotal.toFloat().coerceAtLeast(1f)
                val animatedProgress by animateFloatAsState(
                    targetValue = targetProgress,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "theory_progress",
                )
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(CircleShape),
                    color = modeAccent,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round,
                )
            } else {
                LinearProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(CircleShape),
                    color = semanticColors.endlessAccent,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round,
                )
            }

            // Question Card
            val question = uiState.currentQuestion
            if (question != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    tonalElevation = 3.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "QUESTION #${uiState.questionNumber}",
                                style = MaterialTheme.typography.labelMedium,
                                color = modeAccent,
                                modifier = Modifier.testTag("question_number_label"),
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = modeAccent.copy(alpha = 0.12f),
                            ) {
                                Text(
                                    text = "${question.topic.displayName.uppercase()} • ${question.topic.formulaTag}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = modeAccent,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                )
                            }
                        }

                        Text(
                            text = question.prompt,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.testTag("question_prompt"),
                        )
                    }
                }

                // Answer Area
                when (question) {
                    is TheoryQuestion -> {
                        val feedback = uiState.feedback
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            question.options.forEach { option ->
                                TheoryOptionCard(
                                    option = option,
                                    correctKey = question.correctKey,
                                    feedback = feedback,
                                    onClick = { onSelectTheoryOption(option.key) },
                                )
                            }
                        }
                    }
                    is CalculationQuestion -> {
                        CalculationInputSection(
                            question = question,
                            numericInput = uiState.numericInput,
                            feedback = uiState.feedback,
                            onAppendDigit = onAppendDigit,
                            onBackspaceDigit = onBackspaceDigit,
                            onClearDigits = onClearDigits,
                            onSubmitCalculation = onSubmitCalculation,
                        )
                    }
                }
            }

            // Feedback Banner + Next Button
            AnimatedVisibility(
                visible = uiState.feedback != null,
                enter = fadeIn() + slideInVertically { it / 4 },
            ) {
                uiState.feedback?.let { feedback ->
                    FeedbackBanner(
                        feedback = feedback,
                        isLastTheory = uiState.phase == QuizPhase.Theory &&
                            uiState.theoryIndex + 1 >= uiState.theoryTotal,
                        onNextQuestion = onNextQuestion,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun TheoryOptionCard(
    option: QuizOption,
    correctKey: String,
    feedback: AnswerFeedback?,
    onClick: () -> Unit,
) {
    val semantic = LocalQuizSemanticColors.current
    val isAnswered = feedback != null
    val isThisCorrect = option.key == correctKey
    val wasSelected = feedback?.submittedDisplay?.startsWith("${option.key})") == true

    val targetContainer = when {
        !isAnswered -> MaterialTheme.colorScheme.surface
        isThisCorrect -> semantic.correctContainer
        wasSelected -> semantic.wrongContainer
        else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.55f)
    }

    val targetBorder = when {
        !isAnswered -> MaterialTheme.colorScheme.outlineVariant
        isThisCorrect -> semantic.correct
        wasSelected -> semantic.wrong
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    }

    val containerColor by animateColorAsState(targetContainer, label = "opt_bg")
    val borderColor by animateColorAsState(targetBorder, label = "opt_border")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 58.dp)
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = !isAnswered, onClick = onClick)
            .testTag("option_${option.key}"),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(1.5.dp, borderColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        when {
                            isAnswered && isThisCorrect -> semantic.correct
                            isAnswered && wasSelected -> semantic.wrong
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option.key,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isAnswered && (isThisCorrect || wasSelected)) {
                        Color.White
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                )
            }

            Text(
                text = option.label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )

            if (isAnswered && isThisCorrect) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Correct option",
                    tint = semantic.correct,
                )
            } else if (isAnswered && wasSelected) {
                Icon(
                    imageVector = Icons.Rounded.Cancel,
                    contentDescription = "Wrong option",
                    tint = semantic.wrong,
                )
            }
        }
    }
}

@Composable
private fun CalculationInputSection(
    question: CalculationQuestion,
    numericInput: String,
    feedback: AnswerFeedback?,
    onAppendDigit: (Char) -> Unit,
    onBackspaceDigit: () -> Unit,
    onClearDigits: () -> Unit,
    onSubmitCalculation: () -> Unit,
) {
    val semantic = LocalQuizSemanticColors.current
    val isAnswered = feedback != null
    val displayBorder = when {
        feedback == null -> semantic.endlessAccent.copy(alpha = 0.7f)
        feedback.isCorrect -> semantic.correct
        else -> semantic.wrong
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Numeric Readout Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("numeric_display"),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(1.5.dp, displayBorder),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = numericInput.ifEmpty { "Enter number..." },
                    fontFamily = JetBrainsMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = if (numericInput.isEmpty()) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = semantic.endlessContainer,
                ) {
                    Text(
                        text = question.unit,
                        style = MaterialTheme.typography.labelLarge,
                        color = semantic.endlessAccent,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    )
                }
            }
        }

        // Built-in Tactical Numeric Keypad (hidden once feedback appears to keep screen clean)
        if (!isAnswered) {
            val rows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("CLR", "0", "DEL"),
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                rows.forEach { rowKeys ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        rowKeys.forEach { key ->
                            KeypadButton(
                                label = key,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    when (key) {
                                        "CLR" -> onClearDigits()
                                        "DEL" -> onBackspaceDigit()
                                        else -> onAppendDigit(key.first())
                                    }
                                },
                            )
                        }
                    }
                }
            }

            Button(
                onClick = onSubmitCalculation,
                enabled = numericInput.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("submit_answer_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = semantic.endlessAccent,
                    contentColor = Color(0xFF0B1120),
                ),
            ) {
                Text(
                    text = stringResource(R.string.submit_answer),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

@Composable
private fun KeypadButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val isAction = label == "CLR" || label == "DEL"
    Surface(
        modifier = modifier
            .height(52.dp)
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("keypad_$label"),
        shape = RoundedCornerShape(14.dp),
        color = if (isAction) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (label == "DEL") {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Backspace,
                    contentDescription = "Backspace",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                Text(
                    text = label,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontWeight = if (isAction) FontWeight.Medium else FontWeight.Bold,
                    fontSize = if (isAction) 14.sp else 20.sp,
                    color = if (isAction) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                )
            }
        }
    }
}

@Composable
private fun FeedbackBanner(
    feedback: AnswerFeedback,
    isLastTheory: Boolean,
    onNextQuestion: () -> Unit,
) {
    val semantic = LocalQuizSemanticColors.current
    val bgColor = if (feedback.isCorrect) semantic.correctContainer else semantic.wrongContainer
    val accent = if (feedback.isCorrect) semantic.correct else semantic.wrong

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("feedback_banner"),
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        border = BorderStroke(1.5.dp, accent),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = if (feedback.isCorrect) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(22.dp),
                )
                Text(
                    text = if (feedback.isCorrect) {
                        "Correct!"
                    } else {
                        "Wrong! Correct answer: ${feedback.correctDisplay}"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = accent,
                )
            }

            Text(
                text = feedback.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Button(
                onClick = onNextQuestion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("next_question_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = if (isLastTheory) {
                        stringResource(R.string.enter_endless_mode)
                    } else {
                        stringResource(R.string.next_question)
                    },
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
fun EndlessUnlockScreen(
    theoryScore: Int,
    theoryTotal: Int,
    onLaunchEndless: () -> Unit,
    onFinishQuiz: () -> Unit,
    contentPadding: PaddingValues,
) {
    val semantic = LocalQuizSemanticColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 560.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(semantic.endlessContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocalFireDepartment,
                    contentDescription = "Endless Calculation Mode Unlocked",
                    tint = semantic.endlessAccent,
                    modifier = Modifier.size(44.dp),
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "THEORY GAUNTLET CLEARED!",
                    style = MaterialTheme.typography.labelMedium,
                    color = semantic.endlessAccent,
                )
                Text(
                    text = "Entering Endless Calculation Mode",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "You scored $theoryScore / $theoryTotal across all 15 theory questions. Now test your speed with infinite math-physics problems!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, semantic.endlessAccent.copy(alpha = 0.45f)),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "ACTIVE FORMULAS",
                        style = MaterialTheme.typography.labelSmall,
                        color = semantic.endlessAccent,
                    )
                    FormulaRow("Speed", "v = d ÷ t (m/s)")
                    FormulaRow("Force", "F = m × a (N)")
                    FormulaRow("Work Done", "W = F × d (J)")
                }
            }

            Button(
                onClick = onLaunchEndless,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("launch_endless_button"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = semantic.endlessAccent,
                    contentColor = Color(0xFF0B1120),
                ),
            ) {
                Icon(
                    imageVector = Icons.Rounded.AllInclusive,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Launch Endless Mode",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
            }

            OutlinedButton(
                onClick = onFinishQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("finish_after_theory_button"),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.view_results),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun FormulaRow(name: String, formula: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = formula,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
