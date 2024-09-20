package com.example.chaquitaclla_appmovil_android.forum

import android.util.Log
import com.example.chaquitaclla_appmovil_android.forum.beans.Question
import com.example.chaquitaclla_appmovil_android.forum.interfaces.ForumApi
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException

class QuestionsService {
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

    suspend fun getAllQuestions(): List<Question> {
        return try {
            val questions = api.getQuestions()
            Log.d("QuestionsService","Raw JSON response: $questions")
            questions
        } catch (e: SocketException){
            Log.e("QuestionsService", "Error: ${e.message}")
            emptyList()
        }
    }

}