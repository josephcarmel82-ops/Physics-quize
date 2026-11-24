package com.example.model

enum class PhysicsTopic(
    val displayName: String,
    val formulaTag: String,
) {
    MOTION("Motion", "a = Δv / t"),
    FORCE("Force", "F = m × a"),
    ENERGY("Energy", "KE = ½mv²"),
    LIGHT("Light", "c = 3×10⁸ m/s"),
    SOUND("Sound", "f = 1 / T"),
    ELECTRICITY("Electricity", "V = I × R"),
    GRAVITY("Gravity", "g = 9.8 m/s²"),
    HEAT("Heat", "T(°C)"),
    MATH_SPEED("Math-Physics • Speed", "v = d / t"),
    MATH_FORCE("Math-Physics • Force", "F = m × a"),
    MATH_WORK("Math-Physics • Work", "W = F × d"),
}

data class QuizOption(
    val key: String,
    val label: String,
)

sealed interface QuizQuestion {
    val id: Int
    val topic: PhysicsTopic
    val prompt: String

    data class TheoryQuestion(
        override val id: Int,
        override val topic: PhysicsTopic,
        override val prompt: String,
        val options: List<QuizOption>,
        val correctKey: String,
        val explanation: String,
    ) : QuizQuestion

    data class CalculationQuestion(
        override val id: Int,
        override val topic: PhysicsTopic,
        override val prompt: String,
        val formulaHint: String,
        val unit: String,
        val correctAnswer: Int,
    ) : QuizQuestion
}

data class AnswerFeedback(
    val isCorrect: Boolean,
    val submittedDisplay: String,
    val correctDisplay: String,
    val explanation: String,
)

data class AnsweredRecord(
    val questionNumber: Int,
    val topic: PhysicsTopic,
    val prompt: String,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val isCalculation: Boolean,
)

sealed interface QuizPhase {
    data object Home : QuizPhase
    data object Theory : QuizPhase
    data object EndlessUnlock : QuizPhase
    data object Endless : QuizPhase
    data object GameOver : QuizPhase
}

data class QuizUiState(
    val phase: QuizPhase = QuizPhase.Home,
    val currentQuestion: QuizQuestion? = null,
    val questionNumber: Int = 0,
    val theoryIndex: Int = 0,
    val theoryTotal: Int = 15,
    val theoryScore: Int = 0,
    val endlessScore: Int = 0,
    val endlessAttempted: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val highScore: Int = 0,
    val numericInput: String = "",
    val feedback: AnswerFeedback? = null,
    val history: List<AnsweredRecord> = emptyList(),
) {
    val score: Int get() = theoryScore + endlessScore
    val totalAnswered: Int get() = history.size
    val accuracyPercent: Int
        get() = if (totalAnswered == 0) 0 else ((score * 100f) / totalAnswered).toInt()
}
