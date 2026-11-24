package com.example

import com.example.data.PhysicsQuestionBank
import com.example.model.PhysicsTopic
import com.example.model.QuizPhase
import com.example.model.QuizQuestion.CalculationQuestion
import com.example.model.QuizQuestion.TheoryQuestion
import com.example.viewmodel.QuizViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class PhysicsQuizUnitTest {

    @Test
    fun theoryDeckHasFifteenUniqueQuestionsAndTransitionsToEndless() {
        val viewModel = QuizViewModel(Random(42))
        viewModel.startFullQuiz()

        val seenIds = mutableSetOf<Int>()
        repeat(15) { idx ->
            val state = viewModel.uiState.value
            assertEquals(QuizPhase.Theory, state.phase)
            val q = state.currentQuestion as TheoryQuestion
            assertTrue("Duplicate theory question ID ${q.id}", seenIds.add(q.id))
            assertEquals(idx + 1, state.questionNumber)

            viewModel.submitTheoryAnswer(q.correctKey)
            viewModel.nextQuestion()
        }

        val unlockState = viewModel.uiState.value
        assertEquals(QuizPhase.EndlessUnlock, unlockState.phase)
        assertEquals(15, unlockState.score)
        assertEquals(15, unlockState.totalAnswered)

        viewModel.enterEndlessModeFromUnlock()
        val endlessState = viewModel.uiState.value
        assertEquals(QuizPhase.Endless, endlessState.phase)
        val calc = endlessState.currentQuestion as CalculationQuestion
        assertNotNull(calc)
        assertEquals(16, endlessState.questionNumber)

        // Answer calculation question accurately
        calc.correctAnswer.toString().forEach { digit ->
            viewModel.appendDigit(digit)
        }
        viewModel.submitCalculationAnswer()
        assertEquals(16, viewModel.uiState.value.score)
        assertEquals(16, viewModel.uiState.value.totalAnswered)
    }

    @Test
    fun calculationGeneratorProducesExactIntegerPhysicsProblems() {
        val rng = Random(123)
        val seenTopics = mutableSetOf<PhysicsTopic>()
        repeat(50) { index ->
            val q = PhysicsQuestionBank.generateCalculationQuestion(index, rng)
            seenTopics.add(q.topic)
            assertTrue(q.correctAnswer > 0)
        }
        assertTrue(seenTopics.contains(PhysicsTopic.MATH_SPEED))
        assertTrue(seenTopics.contains(PhysicsTopic.MATH_FORCE))
        assertTrue(seenTopics.contains(PhysicsTopic.MATH_WORK))
    }

    @Test
    fun quitMidQuizPreservesAnsweredTotal() {
        val viewModel = QuizViewModel(Random(7))
        viewModel.startFullQuiz()

        val q1 = viewModel.uiState.value.currentQuestion as TheoryQuestion
        viewModel.submitTheoryAnswer(q1.correctKey)
        viewModel.nextQuestion()

        // Quit on Q2 before answering Q2
        viewModel.quitQuiz()
        val finalState = viewModel.uiState.value
        assertEquals(QuizPhase.GameOver, finalState.phase)
        assertEquals(1, finalState.score)
        assertEquals(1, finalState.totalAnswered)
    }
}
