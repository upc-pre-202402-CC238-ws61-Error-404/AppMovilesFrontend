package com.example.chaquitaclla_appmovil_android.forum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.forum.viewholders.AnswerViewHolder
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.forum.beans.Answer
import com.example.chaquitaclla_appmovil_android.iam.beans.ProfileResponse

class AdapterAnswer(val answersList: List<Answer>, val profileList: List<ProfileResponse>): RecyclerView.Adapter<AnswerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AnswerViewHolder(layoutInflater.inflate(R.layout.answer_item, parent, false), profileList)
    }

    override fun getItemCount(): Int = answersList.size

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val item = answersList[position]
        holder.render(item)
    }
}