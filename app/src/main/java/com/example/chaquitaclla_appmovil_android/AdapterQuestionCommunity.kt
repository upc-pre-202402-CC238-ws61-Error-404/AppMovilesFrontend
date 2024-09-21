package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.forum.beans.Question

class AdapterQuestionCommunity(val questionsList: List<Question>):RecyclerView.Adapter<QuestionCommunityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionCommunityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return QuestionCommunityViewHolder(layoutInflater.inflate(R.layout.community_question_item, parent, false))
    }

    override fun getItemCount(): Int = questionsList.size

    override fun onBindViewHolder(holder: QuestionCommunityViewHolder, position: Int) {
        val item = questionsList[position]
        holder.render(item)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context,AnswersActivity::class.java)
            intent.putExtra("question", item)
            context.startActivity(intent)
        }
    }

}