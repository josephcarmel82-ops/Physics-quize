package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.QuizPhase
import com.example.ui.screens.EndlessUnlockScreen
import com.example.ui.screens.GameOverScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.theme.PhysicsPulseTheme
import com.example.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )
        setContent {
            PhysicsPulseTheme {
                PhysicsPulseApp()
            }
        }
    }
}

@Composable
fun PhysicsPulseApp(
    quizViewModel: QuizViewModel = viewModel(),
) {
    val uiState by quizViewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(enabled = uiState.phase != QuizPhase.Home) {
        when (uiState.phase) {
            QuizPhase.Theory, QuizPhase.Endless, QuizPhase.EndlessUnlock -> quizViewModel.quitQuiz()
            QuizPhase.GameOver -> quizViewModel.returnHome()
            QuizPhase.Home -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        Crossfade(targetState = uiState.phase, label = "quiz_phase_transition") { phase ->
            when (phase) {
                QuizPhase.Home -> HomeScreen(
                    highScore = uiState.highScore,
                    onStartFullQuiz = quizViewModel::startFullQuiz,
                    onStartEndlessDirectly = quizViewModel::startEndlessDirectly,
                    contentPadding = innerPadding,
                )
                QuizPhase.Theory, QuizPhase.Endless -> QuizScreen(
                    uiState = uiState,
                    onSelectTheoryOption = quizViewModel::submitTheoryAnswer,
                    onAppendDigit = quizViewModel::appendDigit,
                    onBackspaceDigit = quizViewModel::backspaceDigit,
                    onClearDigits = quizViewModel::clearDigits,
                    onSubmitCalculation = quizViewModel::submitCalculationAnswer,
                    onNextQuestion = quizViewModel::nextQuestion,
                    onQuitQuiz = quizViewModel::quitQuiz,
                    contentPadding = innerPadding,
                )
                QuizPhase.EndlessUnlock -> EndlessUnlockScreen(
                    theoryScore = uiState.theoryScore,
                    theoryTotal = uiState.theoryTotal,
                    onLaunchEndless = quizViewModel::enterEndlessModeFromUnlock,
                    onFinishQuiz = quizViewModel::quitQuiz,
                    contentPadding = innerPadding,
                )
                QuizPhase.GameOver -> GameOverScreen(
                    uiState = uiState,
                    onPlayAgain = quizViewModel::startFullQuiz,
                    onBackToHome = quizViewModel::returnHome,
                    contentPadding = innerPadding,
                )
            }
        }
    }
}
