package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.PhysicsQuestionBank
import com.example.model.AnswerFeedback
import com.example.model.AnsweredRecord
import com.example.model.QuizPhase
import com.example.model.QuizQuestion.CalculationQuestion
import com.example.model.QuizQuestion.TheoryQuestion
import com.example.model.QuizUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class QuizViewModel(
    private val random: Random = Random.Default,
) : ViewModel() {

    private var shuffledDeck: List<TheoryQuestion> = emptyList()
    private var endlessCounter: Int = 100

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun startFullQuiz() {
        shuffledDeck = PhysicsQuestionBank.shuffledTheoryDeck(random)
        endlessCounter = 100
        val firstQuestion = shuffledDeck.first()
        _uiState.update { current ->
            QuizUiState(
                phase = QuizPhase.Theory,
                currentQuestion = firstQuestion,
                questionNumber = 1,
                theoryIndex = 0,
                theoryTotal = shuffledDeck.size,
                highScore = current.highScore,
            )
        }
    }

    fun startEndlessDirectly() {
        shuffledDeck = emptyList()
        endlessCounter = 100
        val firstCalc = PhysicsQuestionBank.generateCalculationQuestion(++endlessCounter, random)
        _uiState.update { current ->
            QuizUiState(
                phase = QuizPhase.Endless,
                currentQuestion = firstCalc,
                questionNumber = 1,
                theoryIndex = 0,
                theoryTotal = PhysicsQuestionBank.theoryQuestions.size,
                highScore = current.highScore,
            )
        }
    }

    fun submitTheoryAnswer(selectedKey: String) {
        val state = _uiState.value
        val question = state.currentQuestion as? TheoryQuestion ?: return
        if (state.feedback != null) return

        val normalized = selectedKey.trim().uppercase()
        val isCorrect = normalized == question.correctKey
        val selectedOption = question.options.find { it.key == normalized }
        val correctOption = question.options.first { it.key == question.correctKey }

        val newStreak = if (isCorrect) state.currentStreak + 1 else 0
        val newBestStreak = maxOf(state.bestStreak, newStreak)
        val newTheoryScore = if (isCorrect) state.theoryScore + 1 else state.theoryScore
        val newTotalScore = newTheoryScore + state.endlessScore
        val newHighScore = maxOf(state.highScore, newTotalScore)

        val submittedDisplay = selectedOption?.let { "${it.key}) ${it.label}" } ?: normalized
        val correctDisplay = "${correctOption.key}) ${correctOption.label}"

        val record = AnsweredRecord(
            questionNumber = state.questionNumber,
            topic = question.topic,
            prompt = question.prompt,
            userAnswer = submittedDisplay,
            correctAnswer = correctDisplay,
            isCorrect = isCorrect,
            isCalculation = false,
        )

        _uiState.update {
            it.copy(
                theoryScore = newTheoryScore,
                currentStreak = newStreak,
                bestStreak = newBestStreak,
                highScore = newHighScore,
                feedback = AnswerFeedback(
                    isCorrect = isCorrect,
                    submittedDisplay = submittedDisplay,
                    correctDisplay = correctDisplay,
                    explanation = question.explanation,
                ),
                history = it.history + record,
            )
        }
    }

    fun appendDigit(digit: Char) {
        if (!digit.isDigit()) return
        _uiState.update { state ->
            if (state.feedback != null || state.numericInput.length >= 6) return@update state
            val updated = if (state.numericInput == "0") digit.toString() else state.numericInput + digit
            state.copy(numericInput = updated)
        }
    }

    fun backspaceDigit() {
        _uiState.update { state ->
            if (state.feedback != null || state.numericInput.isEmpty()) return@update state
            state.copy(numericInput = state.numericInput.dropLast(1))
        }
    }

    fun clearDigits() {
        _uiState.update { state ->
            if (state.feedback != null) return@update state
            state.copy(numericInput = "")
        }
    }

    fun setNumericInput(raw: String) {
        val digitsOnly = raw.filter { it.isDigit() }.take(6)
        _uiState.update { state ->
            if (state.feedback != null) return@update state
            state.copy(numericInput = digitsOnly)
        }
    }

    fun submitCalculationAnswer() {
        val state = _uiState.value
        val question = state.currentQuestion as? CalculationQuestion ?: return
        if (state.feedback != null || state.numericInput.isBlank()) return

        val parsed = state.numericInput.toIntOrNull() ?: return
        val isCorrect = parsed == question.correctAnswer

        val newStreak = if (isCorrect) state.currentStreak + 1 else 0
        val newBestStreak = maxOf(state.bestStreak, newStreak)
        val newEndlessScore = if (isCorrect) state.endlessScore + 1 else state.endlessScore
        val newTotalScore = state.theoryScore + newEndlessScore
        val newHighScore = maxOf(state.highScore, newTotalScore)

        val submittedDisplay = "$parsed ${question.unit}"
        val correctDisplay = "${question.correctAnswer} ${question.unit}"

        val record = AnsweredRecord(
            questionNumber = state.questionNumber,
            topic = question.topic,
            prompt = question.prompt,
            userAnswer = submittedDisplay,
            correctAnswer = correctDisplay,
            isCorrect = isCorrect,
            isCalculation = true,
        )

        _uiState.update {
            it.copy(
                endlessScore = newEndlessScore,
                endlessAttempted = it.endlessAttempted + 1,
                currentStreak = newStreak,
                bestStreak = newBestStreak,
                highScore = newHighScore,
                feedback = AnswerFeedback(
                    isCorrect = isCorrect,
                    submittedDisplay = submittedDisplay,
                    correctDisplay = correctDisplay,
                    explanation = question.formulaHint,
                ),
                history = it.history + record,
            )
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        when (state.phase) {
            QuizPhase.Theory -> {
                val nextIndex = state.theoryIndex + 1
                if (nextIndex < shuffledDeck.size) {
                    _uiState.update {
                        it.copy(
                            currentQuestion = shuffledDeck[nextIndex],
                            questionNumber = it.questionNumber + 1,
                            theoryIndex = nextIndex,
                            feedback = null,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            phase = QuizPhase.EndlessUnlock,
                            currentQuestion = null,
                            feedback = null,
                        )
                    }
                }
            }
            QuizPhase.Endless -> {
                val nextCalc = PhysicsQuestionBank.generateCalculationQuestion(++endlessCounter, random)
                _uiState.update {
                    it.copy(
                        currentQuestion = nextCalc,
                        questionNumber = it.questionNumber + 1,
                        numericInput = "",
                        feedback = null,
                    )
                }
            }
            else -> Unit
        }
    }

    fun enterEndlessModeFromUnlock() {
        val nextCalc = PhysicsQuestionBank.generateCalculationQuestion(++endlessCounter, random)
        _uiState.update {
            it.copy(
                phase = QuizPhase.Endless,
                currentQuestion = nextCalc,
                questionNumber = it.questionNumber + 1,
                numericInput = "",
                feedback = null,
            )
        }
    }

    fun quitQuiz() {
        _uiState.update { state ->
            state.copy(
                phase = QuizPhase.GameOver,
                currentQuestion = null,
                feedback = null,
                numericInput = "",
            )
        }
    }

    fun returnHome() {
        _uiState.update { state ->
            QuizUiState(
                phase = QuizPhase.Home,
                highScore = state.highScore,
            )
        }
    }
}
