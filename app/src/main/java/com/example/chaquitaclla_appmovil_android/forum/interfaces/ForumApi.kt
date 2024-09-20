package com.example.chaquitaclla_appmovil_android.forum.interfaces

import com.example.chaquitaclla_appmovil_android.forum.beans.Answer
import com.example.chaquitaclla_appmovil_android.forum.beans.Question
import retrofit2.http.GET

interface ForumApi {
    @GET("forum/questions")
    suspend fun getQuestions(): List<Question>

    @GET("forum/questions/{questionId}/answers")
    suspend fun getAnswersByQuestionId(questionId: Int): List<Answer>
}