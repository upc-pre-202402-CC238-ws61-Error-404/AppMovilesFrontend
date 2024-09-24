package com.example.chaquitaclla_appmovil_android

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.forum.adapter.AdapterAnswer
import com.example.chaquitaclla_appmovil_android.forum.services.AnswersService
import com.example.chaquitaclla_appmovil_android.forum.beans.Answer
import com.example.chaquitaclla_appmovil_android.forum.beans.AnswerPost
import com.example.chaquitaclla_appmovil_android.forum.beans.Question
import com.example.chaquitaclla_appmovil_android.forum.services.CategoriesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnswersActivity : AppCompatActivity() {

    private lateinit var answersService: AnswersService
    private lateinit var categoriesService: CategoriesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_answers)

        answersService = AnswersService()
        categoriesService = CategoriesService()



        val question = intent.getSerializableExtra("question") as Question
        val isFromCommunity = intent.getBooleanExtra("isFromCommunity", false)

        setupBackButton()
        displayQuestionDetails(question)
        fetchAndDisplayAnswers(question.questionId)
        setupAddAnswerButton(question, isFromCommunity)
    }

    private fun displayQuestionDetails(question: Question) {
        val txtQuestionAnswers: TextView = findViewById(R.id.txtQuestionAnswers)
        val txtUserQuestion: TextView = findViewById(R.id.txtUserQuestion)
        val txtDateQuestion: TextView = findViewById(R.id.txtDateQuestion)
        val txtCategoryQuestion: TextView = findViewById(R.id.txtCategoryQuestion)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val category = categoriesService.getCategoryById(question.categoryId)
                withContext(Dispatchers.Main) {
                    txtCategoryQuestion.text = category.name
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("AnswersActivity", "Error: ${e.message}")
                }
            }
        }


        txtQuestionAnswers.text = question.questionText
        txtUserQuestion.text = question.authorId.toString()
        txtDateQuestion.text = question.date.toString().take(10)
    }
    private fun setupAddAnswerButton(question: Question, isFromCommunity:Boolean) {
        val addQuestionButton: Button = findViewById(R.id.btnAddAnswer)


        if (isFromCommunity) {
            addQuestionButton.visibility = View.VISIBLE
        } else {
            addQuestionButton.visibility = View.GONE
        }

        addQuestionButton.setOnClickListener {
            showAddAnswerDialog(question)
        }
    }
    private fun showAddAnswerDialog(question: Question) {
        val dialogView = layoutInflater.inflate(R.layout.add_answer_dialog, null)
        val answerDialogTitle: TextView = dialogView.findViewById(R.id.answerDialogTitle)
        val editTextAnswer: EditText = dialogView.findViewById(R.id.editTextAnswer)
        val btnAddAnswer: Button = dialogView.findViewById(R.id.btnAddAnswer)
        val btnCancelAnswer: Button = dialogView.findViewById(R.id.btnCancelAnswer)

        answerDialogTitle.text = "Add Answer"
        btnAddAnswer.text = "Add"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnAddAnswer.setOnClickListener {
            val answerText = editTextAnswer.text.toString()
            val answer= AnswerPost(
                questionId = question.questionId,
                answerText = answerText,
                authorId = 1
            )
            // Aquí puedes agregar la lógica para agregar la respuesta
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    answersService.addAnswer(answer)
                    withContext(Dispatchers.Main) {
                        fetchAndDisplayAnswers(question.questionId)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("AnswersActivity", "Error: ${e.message}")
                    }
                }
            }
            dialog.dismiss()
        }

        btnCancelAnswer.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
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

    private fun setupBackButton() {
        val btnBackForum: ImageButton = findViewById(R.id.btnBackForum)
        btnBackForum.setOnClickListener {
            finish()
        }
    }
}