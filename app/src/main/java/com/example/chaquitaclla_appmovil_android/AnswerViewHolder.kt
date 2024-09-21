package com.example.chaquitaclla_appmovil_android

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.forum.beans.Answer

class AnswerViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val answerCont = view.findViewById<TextView>(R.id.txtContentAnswer)
    val user = view.findViewById<TextView>(R.id.txtUserAnswer)

    fun render(answer: Answer){
        answerCont.text = answer.answerText
        user.text = "Author ID: ${answer.authorId}"
    }
}