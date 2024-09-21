package com.example.chaquitaclla_appmovil_android.forum.interfaces

import com.example.chaquitaclla_appmovil_android.forum.beans.Answer
import com.example.chaquitaclla_appmovil_android.forum.beans.Question
import retrofit2.http.GET
import retrofit2.http.Path

interface ForumApi {
    @GET("forum/questions")
    suspend fun getQuestions(): List<Question>

    @GET("forum/question/{questionId}/answers")
    suspend fun getAnswersByQuestionId(@Path("questionId") questionId: Int): List<Answer>
}