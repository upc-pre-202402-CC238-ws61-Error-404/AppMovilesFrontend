package com.example.chaquitaclla_appmovil_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.forum.beans.Question

class AdapterQuestionUser(val questionsList: List<Question>): RecyclerView.Adapter<QuestionUserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionUserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return QuestionUserViewHolder(layoutInflater.inflate(R.layout.user_question_item, parent, false))
    }

    override fun getItemCount(): Int = questionsList.size

    override fun onBindViewHolder(holder: QuestionUserViewHolder, position: Int) {
        val item = questionsList[position]
        holder.render(item)
    }

}