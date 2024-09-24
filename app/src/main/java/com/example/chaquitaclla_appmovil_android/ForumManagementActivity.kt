package com.example.chaquitaclla_appmovil_android

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.forum.adapter.AdapterQuestionCommunity
import com.example.chaquitaclla_appmovil_android.forum.adapter.AdapterQuestionUser
import com.example.chaquitaclla_appmovil_android.forum.beans.Category
import com.example.chaquitaclla_appmovil_android.forum.beans.DateFormat
import com.example.chaquitaclla_appmovil_android.forum.services.CategoriesService
import com.example.chaquitaclla_appmovil_android.forum.services.QuestionsService
import com.example.chaquitaclla_appmovil_android.forum.beans.Question
import com.example.chaquitaclla_appmovil_android.forum.beans.QuestionPost
import com.example.chaquitaclla_appmovil_android.forum.beans.QuestionPut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class ForumManagementActivity : AppCompatActivity() {

    private lateinit var questionsService: QuestionsService
    private lateinit var categoriesService: CategoriesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum_management)

        questionsService = QuestionsService()
        categoriesService = CategoriesService()


        setupAutoCompleteTextView()
        setupAddQuestionButton()
        fetchAndDisplayQuestionsCommunity()
    }

    private fun setupAutoCompleteTextView() {
        val item = listOf("Community", "My Questions")
        val autoComplete: AutoCompleteTextView = findViewById(R.id.spinnerForum)

        val adapter = ArrayAdapter(this, R.layout.section_forum_list, item)

        autoComplete.setAdapter(adapter)
        autoComplete.setText(item[0], false)
        autoComplete.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            if (selectedItem == "Community") {
                clearQuestions()
                fetchAndDisplayQuestionsCommunity()
            }else if (selectedItem == "My Questions") {
                clearQuestions()
                fetchAndDisplayQuestionsUser()
            }
            Toast.makeText(this, " $selectedItem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAddQuestionButton() {
        val addQuestionButton: Button = findViewById(R.id.btnAddQuestion)
        addQuestionButton.setOnClickListener {
            showAddQuestionDialog()
        }
    }

    private fun showAddQuestionDialog(categories: List<String> = emptyList()) {
        val dialogView = layoutInflater.inflate(R.layout.add_edit_question_dialog, null)
        val editTextQuestion: EditText = dialogView.findViewById(R.id.editTextQuestion)
        val spinnerCategory: AutoCompleteTextView = dialogView.findViewById(R.id.spinnerCategory)
        val btnAddQuestion: Button = dialogView.findViewById(R.id.btnAddQuestion)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)

        val questionDialogTitle: TextView = dialogView.findViewById(R.id.questionDialogTitle)

        // Establecer el texto del TextView
        questionDialogTitle.text = "Add New Question"
        btnAddQuestion.text = "Add"


        var selectedCategoryId: Int = 0

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categories = categoriesService.getAllCategories().map { Pair(it.categoryId, it.name) }
                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(this@ForumManagementActivity, android.R.layout.simple_dropdown_item_1line, categories.map { it.second })
                    spinnerCategory.setAdapter(adapter)


                    spinnerCategory.setOnItemClickListener { parent, view, position, id ->
                        selectedCategoryId = categories[position].first
                        // Almacena el ID de la categoría seleccionada para su uso posterior
                        // Puedes usar `selectedCategoryId` donde lo necesites
                    }
                }
            } catch (e: Exception) {
                Log.e("ForumManagementActivity", "Error: ${e.message}")
            }
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnAddQuestion.setOnClickListener {
            val questionText = editTextQuestion.text.toString()
            val categoryId = selectedCategoryId
            val date = DateFormat.format(Date())

            //TODO: Cambiar el id del usuario
            val question = QuestionPost(1, categoryId, questionText, date)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    questionsService.addQuestion(question)
                    withContext(Dispatchers.Main) {
                        fetchAndDisplayQuestionsCommunity()
                        Toast.makeText(this@ForumManagementActivity, "Question added successfully", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ForumManagementActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showEditQuestionDialog(question: Question, categoryName: String){
        val dialogView = layoutInflater.inflate(R.layout.add_edit_question_dialog, null)
        val editTextQuestion: EditText = dialogView.findViewById(R.id.editTextQuestion)
        val spinnerCategory: AutoCompleteTextView = dialogView.findViewById(R.id.spinnerCategory)
        val btnAddQuestion: Button = dialogView.findViewById(R.id.btnAddQuestion)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)

        val questionDialogTitle: TextView = dialogView.findViewById(R.id.questionDialogTitle)
        questionDialogTitle.text = "Edit Question"
        btnAddQuestion.text = "Update"

        editTextQuestion.setText(question.questionText)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categories = categoriesService.getAllCategories().map { Pair(it.categoryId, it.name) }
                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(this@ForumManagementActivity, android.R.layout.simple_dropdown_item_1line, categories.map { it.second })
                    spinnerCategory.setAdapter(adapter)
                    spinnerCategory.setText(categoryName, false)

                    spinnerCategory.setOnItemClickListener { parent, view, position, id ->
                        question.categoryId = categories[position].first
                    }
                }
            } catch (e: Exception) {
                Log.e("ForumManagementActivity", "Error: ${e.message}")
            }
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnAddQuestion.setOnClickListener {
            question.questionText = editTextQuestion.text.toString()
            val updatedQuestion = QuestionPut(question.categoryId, question.questionText)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    questionsService.updateQuestion(question.questionId, updatedQuestion)
                    withContext(Dispatchers.Main) {
                        fetchAndDisplayQuestionsUser()
                        Toast.makeText(this@ForumManagementActivity, "Question updated successfully", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ForumManagementActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showDeleteQuestionDialog(question: Question) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Delete Question")
            .setMessage("Are you sure you want to delete this question?")
            .setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        questionsService.deleteQuestion(question.questionId)
                        withContext(Dispatchers.Main) {
                            fetchAndDisplayQuestionsUser()
                            Toast.makeText(this@ForumManagementActivity, "Question deleted successfully", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@ForumManagementActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("No", null)
            .create()

        dialog.show()
    }

    private fun fetchAndDisplayQuestionsCommunity(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val questions = questionsService.getAllQuestions()
                withContext(Dispatchers.Main){
                    displayQuestionsCommunity(questions)
                }
            } catch (e: Exception){
                Log.e("CommunityQuestionActivity", "Error: ${e.message}")
            }
        }
    }

    private fun displayQuestionsCommunity(questions: List<Question>){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewQuestions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AdapterQuestionCommunity(questions)

    }
    private fun clearQuestions() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewQuestions)
        recyclerView.adapter = AdapterQuestionCommunity(emptyList())
    }

    private fun fetchAndDisplayQuestionsUser(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categories = categoriesService.getAllCategories()
                //TODO: Cambiar el id del usuario
                val questions = questionsService.getQuestionsByAuthorId(1)
                withContext(Dispatchers.Main){
                    displayQuestionsUser(questions, categories)
                }
            } catch (e: Exception){
                Log.e("ForumActivity", "Error: ${e.message}")
            }
        }
    }

    private fun displayQuestionsUser(questions: List<Question>, categories: List<Category>){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewQuestions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AdapterQuestionUser(questions, categories)

    }
}