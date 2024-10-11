package com.example.chaquitaclla_appmovil_android.forum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.forum.viewholders.QuestionUserViewHolder
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.forum.beans.Category
import com.example.chaquitaclla_appmovil_android.forum.beans.Question
import com.example.chaquitaclla_appmovil_android.iam.beans.ProfileResponse

class AdapterQuestionUser(
    val questionsList: List<Question>,
    val categoryiesList: List<Category>): RecyclerView.Adapter<QuestionUserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionUserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return QuestionUserViewHolder(layoutInflater.inflate(R.layout.user_question_item, parent, false), categoryiesList)
    }

    override fun getItemCount(): Int = questionsList.size

    override fun onBindViewHolder(holder: QuestionUserViewHolder, position: Int) {
        val item = questionsList[position]
        holder.render(item)
    }

}