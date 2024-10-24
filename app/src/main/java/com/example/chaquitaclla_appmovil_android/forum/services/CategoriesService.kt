package com.example.chaquitaclla_appmovil_android.forum.services

import android.util.Log
import com.example.chaquitaclla_appmovil_android.SessionManager
import com.example.chaquitaclla_appmovil_android.forum.beans.Category
import com.example.chaquitaclla_appmovil_android.forum.interfaces.ForumApi
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoriesService {
    val dotenv = dotenv(){
        directory = "/assets"
        filename = "env"
    }
    private val api: ForumApi
    private val token = SessionManager.token

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

    suspend fun getAllCategories(): List<Category> {
        return try {
            val categories = api.getCategories()
            Log.d("CategoriesService","Raw JSON response: $categories")
            categories
        } catch (e: Exception){
            Log.e("CategoriesService", "Error: ${e.message}")
            emptyList()
        }
    }

    suspend fun getCategoryById(categoryId: Int): Category {
        return try {
            val category = api.getCategoryById(categoryId)
            Log.d("CategoriesService","Raw JSON response: $category")
            category
        } catch (e: Exception){
            Log.e("CategoriesService", "Error: ${e.message}")
            Category(0,"")
        }
    }

}