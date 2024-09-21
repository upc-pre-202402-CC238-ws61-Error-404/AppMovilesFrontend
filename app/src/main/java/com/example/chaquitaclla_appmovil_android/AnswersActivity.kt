package com.example.chaquitaclla_appmovil_android

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.forum.AnswersService
import com.example.chaquitaclla_appmovil_android.forum.beans.Answer
import com.example.chaquitaclla_appmovil_android.forum.beans.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnswersActivity : AppCompatActivity() {

    private lateinit var answersService: AnswersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_answers)

        answersService = AnswersService()

        val question = intent.getSerializableExtra("question") as Question

        displayQuestionDetails(question)
        fetchAndDisplayAnswers(question.questionId)
    }

    private fun displayQuestionDetails(question: Question) {
        val txtQuestionAnswers: TextView = findViewById(R.id.txtQuestionAnswers)
        val txtUserQuestion: TextView = findViewById(R.id.txtUserQuestion)
        val txtDateQuestion: TextView = findViewById(R.id.txtDateQuestion)

        txtQuestionAnswers.text = question.questionText
        txtUserQuestion.text = "Author ID: ${question.authorId}"
        txtDateQuestion.text = question.date
    }

    private fun fetchAndDisplayAnswers(questionId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val answers = answersService.getAllAnswersByQuestionId(questionId)
                withContext(Dispatchers.Main) {
                    displayAnswers(answers)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("AnswersActivity", "Error: ${e.message}")
                }
            }
        }
    }

    private fun displayAnswers(answers: List<Answer>) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewAnswers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AdapterAnswer(answers)

    }
}