package com.example.chaquitaclla_appmovil_android.forum.services

import android.util.Log
import com.example.chaquitaclla_appmovil_android.forum.beans.Answer
import com.example.chaquitaclla_appmovil_android.forum.beans.AnswerPost
import com.example.chaquitaclla_appmovil_android.forum.interfaces.ForumApi
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException

class AnswersService {
    val dotenv = dotenv(){
        directory = "/assets"
        filename = "env"
    }
    private val api: ForumApi
    private val token = dotenv["BEARER_TOKEN"]

    init {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Authorization", "Bearer $token")
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(dotenv["API_URL"])
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ForumApi::class.java)
    }

    suspend fun getAllAnswersByQuestionId(questionId: Int): List<Answer> {
        return try {
            val answers = api.getAnswersByQuestionId(questionId)
            Log.d("AnswersService","Raw JSON response: $answers")
            answers
        } catch (e: SocketException){
            Log.e("AnswersService", "Error: ${e.message}")
            emptyList()
        }
    }

    suspend fun addAnswer(answer: AnswerPost) {
        try {
            api.addAnswer(answer)
        } catch (e: SocketException){
            Log.e("AnswersService", "Error: ${e.message}")
        }
    }

}