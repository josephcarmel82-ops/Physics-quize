package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
data class QuizSemanticColors(
    val correct: Color,
    val correctContainer: Color,
    val wrong: Color,
    val wrongContainer: Color,
    val endlessAccent: Color,
    val endlessContainer: Color,
)

val LocalQuizSemanticColors = staticCompositionLocalOf {
    QuizSemanticColors(
        correct = EmeraldCorrect,
        correctContainer = EmeraldContainerDark,
        wrong = CoralWrong,
        wrongContainer = CoralContainerDark,
        endlessAccent = KineticAmber,
        endlessContainer = AmberContainerDark,
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = ObsidianNavy,
    primaryContainer = CyanContainerDark,
    onPrimaryContainer = ElectricCyan,
    secondary = KineticAmber,
    onSecondary = ObsidianNavy,
    secondaryContainer = AmberContainerDark,
    onSecondaryContainer = KineticAmber,
    tertiary = IonViolet,
    onTertiary = ObsidianNavy,
    tertiaryContainer = VioletContainerDark,
    onTertiaryContainer = IonViolet,
    background = ObsidianNavy,
    onBackground = FrostWhite,
    surface = DeepSlate,
    onSurface = FrostWhite,
    surfaceVariant = ElevatedSlate,
    onSurfaceVariant = SlateMutedText,
    outline = SlateBorder,
    outlineVariant = SlateBorder.copy(alpha = 0.5f),
    error = CoralWrong,
    onError = FrostWhite,
    errorContainer = CoralContainerDark,
    onErrorContainer = CoralWrong,
)

private val LightColorScheme = lightColorScheme(
    primary = CobaltPrimary,
    onPrimary = Color.White,
    primaryContainer = CobaltContainerLight,
    onPrimaryContainer = CobaltPrimary,
    secondary = AmberLight,
    onSecondary = Color.White,
    secondaryContainer = AmberContainerLight,
    onSecondaryContainer = AmberLight,
    tertiary = VioletLight,
    onTertiary = Color.White,
    tertiaryContainer = VioletContainerLight,
    onTertiaryContainer = VioletLight,
    background = LabPaper,
    onBackground = InkDark,
    surface = LabSurface,
    onSurface = InkDark,
    surfaceVariant = LabSurfaceVariant,
    onSurfaceVariant = InkMuted,
    outline = LabOutline,
    outlineVariant = LabOutline.copy(alpha = 0.6f),
    error = CoralWrongLight,
    onError = Color.White,
    errorContainer = CoralContainerLight,
    onErrorContainer = CoralWrongLight,
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@Composable
fun PhysicsPulseTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val useDark = darkTheme || isSystemInDarkTheme()
    val colorScheme = if (useDark) DarkColorScheme else LightColorScheme
    val semanticColors = if (useDark) {
        QuizSemanticColors(
            correct = EmeraldCorrect,
            correctContainer = EmeraldContainerDark,
            wrong = CoralWrong,
            wrongContainer = CoralContainerDark,
            endlessAccent = KineticAmber,
            endlessContainer = AmberContainerDark,
        )
    } else {
        QuizSemanticColors(
            correct = EmeraldCorrectLight,
            correctContainer = EmeraldContainerLight,
            wrong = CoralWrongLight,
            wrongContainer = CoralContainerLight,
            endlessAccent = AmberLight,
            endlessContainer = AmberContainerLight,
        )
    }

    CompositionLocalProvider(LocalQuizSemanticColors provides semanticColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content,
        )
    }
}
