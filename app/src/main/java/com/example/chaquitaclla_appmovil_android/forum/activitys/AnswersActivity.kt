package com.example.chaquitaclla_appmovil_android.forum.activitys

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.BaseActivity
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.SessionManager
import com.example.chaquitaclla_appmovil_android.forum.adapter.AdapterAnswer
import com.example.chaquitaclla_appmovil_android.forum.services.AnswersService
import com.example.chaquitaclla_appmovil_android.forum.beans.Answer
import com.example.chaquitaclla_appmovil_android.forum.beans.AnswerPost
import com.example.chaquitaclla_appmovil_android.forum.beans.Question
import com.example.chaquitaclla_appmovil_android.forum.services.CategoriesService
import com.example.chaquitaclla_appmovil_android.forum.services.ProfileServiceForum
import com.example.chaquitaclla_appmovil_android.iam.beans.ProfileResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnswersActivity : BaseActivity() {

    private lateinit var answersService: AnswersService
    private lateinit var categoriesService: CategoriesService
    private lateinit var profileService: ProfileServiceForum
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_answers, findViewById(R.id.container))

        answersService = AnswersService()
        categoriesService = CategoriesService()
        profileService = ProfileServiceForum()


        val question = intent.getSerializableExtra("question") as Question
        val isFromCommunity = intent.getBooleanExtra("isFromCommunity", false)
        val profileId = SessionManager.profileId?:-1

        setupBackButton()
        displayQuestionDetails(question, profileId)
        fetchAndDisplayAnswers(question.questionId, profileId)
        setupAddAnswerButton(question, isFromCommunity, profileId)
    }

    private fun displayQuestionDetails(question: Question, profileId: Int) {
        val txtQuestionAnswers: TextView = findViewById(R.id.txtQuestionAnswers)
        val txtUserQuestion: TextView = findViewById(R.id.txtUserQuestion)
        val txtDateQuestion: TextView = findViewById(R.id.txtDateQuestion)
        val txtCategoryQuestion: TextView = findViewById(R.id.txtCategoryQuestion)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val category = categoriesService.getCategoryById(question.categoryId)
                val profile = profileService.getProfileById(profileId)
                withContext(Dispatchers.Main) {
                    txtCategoryQuestion.text = category.name
                    txtUserQuestion.text = profile?.fullName ?: "Unknown"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("AnswersActivity", "Error: ${e.message}")
                }
            }
        }


        txtQuestionAnswers.text = question.questionText

        txtDateQuestion.text = question.date.toString().take(10)
    }
    private fun setupAddAnswerButton(question: Question, isFromCommunity:Boolean, profileId: Int) {
        val addQuestionButton: Button = findViewById(R.id.btnAddAnswer)


        if (isFromCommunity) {
            addQuestionButton.visibility = View.VISIBLE
        } else {
            addQuestionButton.visibility = View.GONE
        }

        addQuestionButton.setOnClickListener {
            showAddAnswerDialog(question, profileId)
        }
    }
    private fun showAddAnswerDialog(question: Question, profileId: Int) {
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

            if (answerText.isBlank()) {
                Toast.makeText(this, "Please enter the answer content", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val answer= AnswerPost(
                questionId = question.questionId,
                answerText = answerText,
                authorId = profileId
            )
            // Aquí puedes agregar la lógica para agregar la respuesta
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    answersService.addAnswer(answer)
                    withContext(Dispatchers.Main) {
                        fetchAndDisplayAnswers(question.questionId, profileId)
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

    private fun fetchAndDisplayAnswers(questionId: Int, profileId:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val answers = answersService.getAllAnswersByQuestionId(questionId)
                val profile = profileService.getProfileById(profileId)
                if (profile != null) {
                    withContext(Dispatchers.Main) {
                        displayAnswers(answers, profile)
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("AnswersActivity", "Error: ${e.message}")
                }
            }
        }
    }

    private fun displayAnswers(answers: List<Answer>, profile: ProfileResponse) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewAnswers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AdapterAnswer(answers, profile)

    }

    private fun setupBackButton() {
        val btnBackForum: ImageButton = findViewById(R.id.btnBackForum)
        btnBackForum.setOnClickListener {
            finish()
        }
    }
}