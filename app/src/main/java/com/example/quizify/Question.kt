package com.example.quizify
data class Question(
    val question: String,
    val choices: List<String>,
    val correctAnswer: String
)
