package com.example.chaquitaclla_appmovil_android

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.forum.QuestionsService
import com.example.chaquitaclla_appmovil_android.forum.beans.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForumManagementActivity : AppCompatActivity() {

    private lateinit var questionsService: QuestionsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum_management)

        questionsService = QuestionsService()

        setupAutoCompleteTextView()

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

    private fun fetchAndDisplayQuestionsCommunity(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val questions = questionsService.getAllQuestions()
                println(questions)
                withContext(Dispatchers.Main){
                    displayQuestionsCommunity(questions)
                }
            } catch (e: Exception){
                Log.e("CommunityQuestionActivity", "Error: ${e.message}")
            }
        }
    }

    private fun displayQuestionsCommunity(questions: List<Question>){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewComm)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AdapterQuestionCommunity(questions)

    }
    private fun clearQuestions() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewComm)
        recyclerView.adapter = AdapterQuestionCommunity(emptyList())
    }

    private fun fetchAndDisplayQuestionsUser(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val questions = questionsService.getAllQuestions()
                println(questions)
                withContext(Dispatchers.Main){
                    displayQuestionsUser(questions)
                }
            } catch (e: Exception){
                Log.e("ForumActivity", "Error: ${e.message}")
            }
        }
    }

    private fun displayQuestionsUser(questions: List<Question>){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewComm)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AdapterQuestionUser(questions)

    }
}