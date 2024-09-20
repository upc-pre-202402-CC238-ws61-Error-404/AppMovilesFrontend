package com.example.chaquitaclla_appmovil_android

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.forum.beans.Question

class QuestionUserViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val questionCont = view.findViewById<TextView>(R.id.txtQuestionUser)
    val category = view.findViewById<TextView>(R.id.txtCategory)
    val date = view.findViewById<TextView>(R.id.txtQuestionDate)

    fun render(question: Question){
        questionCont.text = question.questionText
        category.text = question.categoryId.toString()
        date.text = question.date.toString().take(10)
    }

}