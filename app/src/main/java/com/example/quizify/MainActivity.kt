package com.example.quizify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            var showSplash by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                delay(3000) // Delay for 3 seconds
                showSplash = false
            }

            if (showSplash) {
                SplashScreen()
            } else {
                QuizApp()
            }
        }
    }

    @Composable
    fun SplashScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF87CEEB)), // Sky blue background color
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img), // Replace with your image resource
                contentDescription = "Splash Image"
            )
        }
    }

    @Composable
    fun QuizApp() {
        val questions = loadQuestions()
        var currentQuestionIndex by remember { mutableStateOf(getSavedQuestionIndex()) }
        var timeLeft by remember { mutableStateOf(getSavedTimeLeft()) }
        var timerStarted by remember { mutableStateOf(false) }
        var score by remember { mutableStateOf(0) }
        var showResults by remember { mutableStateOf(false) }
        var showCorrectAnswers by remember { mutableStateOf(false) }
        var answers by remember { mutableStateOf(Array(questions.size) { "" }) }

        LaunchedEffect(timerStarted) {
            if (timerStarted) {
                while (timeLeft > 0) {
                    delay(1000L)
                    timeLeft--
                    saveTimeLeft(timeLeft)
                }
                showResults = true
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF87CEEB)) // Sky blue background color
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_image), // Replace with your image resource
                contentDescription = "Quiz Image"
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!showResults) {
                QuestionView(
                    question = questions[currentQuestionIndex],
                    selectedAnswer = answers[currentQuestionIndex],
                    onAnswerSelected = { answer ->
                        answers[currentQuestionIndex] = answer
                    },
                    onNext = {
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            saveQuestionIndex(currentQuestionIndex)
                        } else {
                            showResults = true
                            score = calculateScore(questions, answers)
                            saveScore(score)
                        }
                    },
                    onPrevious = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
                            saveQuestionIndex(currentQuestionIndex)
                        }
                    },
                    currentQuestionIndex = currentQuestionIndex,
                    totalQuestions = questions.size
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Time Left: ${timeLeft / 60}:${(timeLeft % 60).toString().padStart(2, '0')}")
                if (!timerStarted) {
                    Button(onClick = { timerStarted = true }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFFFA500),
                        contentColor = Color.Black
                    )) {
                        Text("Start Quiz")
                    }
                }
            } else {
                if (showCorrectAnswers) {
                    questions.forEachIndexed { index, question ->
                        Text("Question ${index + 1}: ${question.question}")
                        Text("Your Answer: ${answers[index]}")
                        Text("Correct Answer: ${question.correctAnswer}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text("Your Score: $score/${questions.size}")
                } else {
                    Text("Quiz Completed")
                    Text("Your Score: $score/${questions.size}")
                    Button(onClick = {
                        showCorrectAnswers = true
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFFFA500),
                        contentColor = Color.Black
                    )) {
                        Text("Check Correct Answers")
                    }
                }
                Button(onClick = {
                    resetQuizState()
                    currentQuestionIndex = 0
                    timeLeft = 600
                    timerStarted = false
                    score = 0
                    showResults = false
                    showCorrectAnswers = false
                    answers = Array(questions.size) { "" }
                }, colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFFFA500),
                    contentColor = Color.Black
                )) {
                    Text("Restart")
                }
            }
        }
    }

    @Composable
    fun QuestionView(
        question: Question,
        selectedAnswer: String?,
        onAnswerSelected: (String) -> Unit,
        onNext: () -> Unit,
        onPrevious: () -> Unit,
        currentQuestionIndex: Int,
        totalQuestions: Int
    ) {
        Text(question.question)
        Spacer(modifier = Modifier.height(8.dp))
        question.choices.forEach { choice ->
            Button(
                onClick = { onAnswerSelected(choice) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedAnswer == choice) Color.LightGray else Color.White
                )
            ) {
                Text(choice)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onPrevious, enabled = currentQuestionIndex > 0, colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.Black
            )) {
                Text("Previous")
            }
            Button(onClick = onNext, enabled = selectedAnswer != null, colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.Black
            )) {
                Text(if (currentQuestionIndex < totalQuestions - 1) "Next" else "Finish")
            }
        }
    }

    private fun loadQuestions(): List<Question> {
        val inputStream = assets.open("questions.json")
        val reader = InputStreamReader(inputStream)
        val questionType = object : TypeToken<List<Question>>() {}.type
        return Gson().fromJson(reader, questionType)
    }

    private fun getSavedQuestionIndex(): Int {
        val prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE)
        return prefs.getInt("current_question_index", 0)
    }

    private fun saveQuestionIndex(index: Int) {
        val prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE)
        with(prefs.edit()) {
            putInt("current_question_index", index)
            apply()
        }
    }

    private fun getSavedTimeLeft(): Int {
        val prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE)
        return prefs.getInt("time_left", 600) // Default to 10 minutes
    }

    private fun saveTimeLeft(time: Int) {
        val prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE)
        with(prefs.edit()) {
            putInt("time_left", time)
            apply()
        }
    }

    private fun resetQuizState() {
        val prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE)
        with(prefs.edit()) {
            putInt("current_question_index", 0)
            putInt("time_left", 600) // Reset to 10 minutes
            apply()
        }
    }

    private fun calculateScore(questions: List<Question>, answers: Array<String>): Int {
        return answers.zip(questions).count { it.first == it.second.correctAnswer }
    }

    private fun saveScore(score: Int) {
        val prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE)
        with(prefs.edit()) {
            putInt("quiz_score", score)
            apply()
        }
    }
}
