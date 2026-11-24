package com.example.data

import com.example.model.PhysicsTopic
import com.example.model.QuizOption
import com.example.model.QuizQuestion.CalculationQuestion
import com.example.model.QuizQuestion.TheoryQuestion
import kotlin.random.Random

object PhysicsQuestionBank {

    val theoryQuestions: List<TheoryQuestion> = listOf(
        TheoryQuestion(
            id = 1,
            topic = PhysicsTopic.MOTION,
            prompt = "SI unit of displacement?",
            options = listOf(
                QuizOption("A", "kg"),
                QuizOption("B", "m/s"),
                QuizOption("C", "m"),
                QuizOption("D", "s"),
            ),
            correctKey = "C",
            explanation = "Displacement measures shortest straight-line length, so its SI unit is meter (m).",
        ),
        TheoryQuestion(
            id = 2,
            topic = PhysicsTopic.MOTION,
            prompt = "Formula for acceleration?",
            options = listOf(
                QuizOption("A", "Change in velocity / Time"),
                QuizOption("B", "Distance / Time"),
            ),
            correctKey = "A",
            explanation = "Acceleration is the rate of change of velocity with respect to time: a = Δv / t.",
        ),
        TheoryQuestion(
            id = 3,
            topic = PhysicsTopic.MOTION,
            prompt = "Can displacement be zero if distance is not zero?",
            options = listOf(
                QuizOption("A", "Yes"),
                QuizOption("B", "No"),
            ),
            correctKey = "A",
            explanation = "Yes! Returning to the starting point gives zero net displacement even after covering distance.",
        ),
        TheoryQuestion(
            id = 4,
            topic = PhysicsTopic.FORCE,
            prompt = "SI unit of Force?",
            options = listOf(
                QuizOption("A", "Joule"),
                QuizOption("B", "Watt"),
                QuizOption("C", "Newton"),
                QuizOption("D", "Pascal"),
            ),
            correctKey = "C",
            explanation = "Force is measured in Newtons (N), where 1 N = 1 kg·m/s².",
        ),
        TheoryQuestion(
            id = 5,
            topic = PhysicsTopic.FORCE,
            prompt = "Newton's 2nd Law formula?",
            options = listOf(
                QuizOption("A", "F = m * a"),
                QuizOption("B", "F = m / a"),
                QuizOption("C", "F = m + a"),
            ),
            correctKey = "A",
            explanation = "Net force equals mass multiplied by acceleration: F = m × a.",
        ),
        TheoryQuestion(
            id = 6,
            topic = PhysicsTopic.ENERGY,
            prompt = "Energy possessed by a moving object?",
            options = listOf(
                QuizOption("A", "Potential"),
                QuizOption("B", "Kinetic"),
                QuizOption("C", "Chemical"),
            ),
            correctKey = "B",
            explanation = "Kinetic energy is the energy of motion: KE = ½mv².",
        ),
        TheoryQuestion(
            id = 7,
            topic = PhysicsTopic.ENERGY,
            prompt = "SI unit of Work and Energy?",
            options = listOf(
                QuizOption("A", "Newton"),
                QuizOption("B", "Joule"),
                QuizOption("C", "Watt"),
            ),
            correctKey = "B",
            explanation = "Both Work and Energy share the Joule (J), equal to 1 N·m.",
        ),
        TheoryQuestion(
            id = 8,
            topic = PhysicsTopic.LIGHT,
            prompt = "Speed of light in vacuum?",
            options = listOf(
                QuizOption("A", "3 x 10^8 m/s"),
                QuizOption("B", "3 x 10^5 m/s"),
                QuizOption("C", "340 m/s"),
            ),
            correctKey = "A",
            explanation = "Light travels through a vacuum at approximately 3 × 10⁸ m/s.",
        ),
        TheoryQuestion(
            id = 9,
            topic = PhysicsTopic.LIGHT,
            prompt = "Which mirror is used as a rear-view mirror in vehicles?",
            options = listOf(
                QuizOption("A", "Concave"),
                QuizOption("B", "Convex"),
                QuizOption("C", "Plane"),
            ),
            correctKey = "B",
            explanation = "Convex mirrors diverge light outward, giving drivers a wider field of view.",
        ),
        TheoryQuestion(
            id = 10,
            topic = PhysicsTopic.SOUND,
            prompt = "Sound cannot travel through:",
            options = listOf(
                QuizOption("A", "Solids"),
                QuizOption("B", "Water"),
                QuizOption("C", "Vacuum"),
            ),
            correctKey = "C",
            explanation = "Sound is a mechanical wave and requires particles in a medium to propagate.",
        ),
        TheoryQuestion(
            id = 11,
            topic = PhysicsTopic.SOUND,
            prompt = "SI unit of frequency?",
            options = listOf(
                QuizOption("A", "Hertz (Hz)"),
                QuizOption("B", "Decibel (dB)"),
                QuizOption("C", "Meter (m)"),
            ),
            correctKey = "A",
            explanation = "Frequency is measured in Hertz (Hz), representing cycles per second.",
        ),
        TheoryQuestion(
            id = 12,
            topic = PhysicsTopic.ELECTRICITY,
            prompt = "SI unit of electric current?",
            options = listOf(
                QuizOption("A", "Volt"),
                QuizOption("B", "Ohm"),
                QuizOption("C", "Ampere"),
            ),
            correctKey = "C",
            explanation = "Electric current is measured in Amperes (A), equivalent to 1 Coulomb per second.",
        ),
        TheoryQuestion(
            id = 13,
            topic = PhysicsTopic.ELECTRICITY,
            prompt = "Ohm's Law formula?",
            options = listOf(
                QuizOption("A", "V = I * R"),
                QuizOption("B", "V = I / R"),
            ),
            correctKey = "A",
            explanation = "Voltage equals current multiplied by resistance: V = I × R.",
        ),
        TheoryQuestion(
            id = 14,
            topic = PhysicsTopic.GRAVITY,
            prompt = "Value of 'g' on Earth?",
            options = listOf(
                QuizOption("A", "9.8 m/s²"),
                QuizOption("B", "6.67 m/s²"),
                QuizOption("C", "100 m/s²"),
            ),
            correctKey = "A",
            explanation = "Standard gravitational acceleration near Earth's surface is 9.8 m/s².",
        ),
        TheoryQuestion(
            id = 15,
            topic = PhysicsTopic.HEAT,
            prompt = "Boiling point of water in Celsius?",
            options = listOf(
                QuizOption("A", "50°C"),
                QuizOption("B", "100°C"),
                QuizOption("C", "212°C"),
            ),
            correctKey = "B",
            explanation = "At 1 atm pressure, pure water boils at 100°C (212°F).",
        ),
    )

    fun shuffledTheoryDeck(random: Random = Random.Default): List<TheoryQuestion> =
        theoryQuestions.shuffled(random)

    fun generateCalculationQuestion(
        questionId: Int,
        random: Random = Random.Default,
    ): CalculationQuestion {
        return when (random.nextInt(3)) {
            0 -> {
                val timeChoices = listOf(2, 4, 5, 10)
                val t = timeChoices[random.nextInt(timeChoices.size)]
                val d = if (t == 4) {
                    random.nextInt(1, 11) * 20
                } else {
                    random.nextInt(2, 21) * 10
                }
                val ans = d / t
                CalculationQuestion(
                    id = questionId,
                    topic = PhysicsTopic.MATH_SPEED,
                    prompt = "A car travels $d meters in $t seconds. What is its speed (in m/s)?",
                    formulaHint = "Speed = Distance ÷ Time  ($d ÷ $t)",
                    unit = "m/s",
                    correctAnswer = ans,
                )
            }
            1 -> {
                val m = random.nextInt(2, 16)
                val a = random.nextInt(2, 11)
                val ans = m * a
                CalculationQuestion(
                    id = questionId,
                    topic = PhysicsTopic.MATH_FORCE,
                    prompt = "Find Force (in N) for a $m kg mass accelerating at $a m/s².",
                    formulaHint = "Force = Mass × Acceleration  ($m × $a)",
                    unit = "N",
                    correctAnswer = ans,
                )
            }
            else -> {
                val f = random.nextInt(5, 26)
                val d = random.nextInt(2, 11)
                val ans = f * d
                CalculationQuestion(
                    id = questionId,
                    topic = PhysicsTopic.MATH_WORK,
                    prompt = "Find Work Done (in J) when $f N force moves an object $d m.",
                    formulaHint = "Work = Force × Distance  ($f × $d)",
                    unit = "J",
                    correctAnswer = ans,
                )
            }
        }
    }
}
