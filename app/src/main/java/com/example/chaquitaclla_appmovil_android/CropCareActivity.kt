// app/src/main/java/com/example/chaquitaclla_appmovil_android/CropCaresActivity.kt
package com.example.chaquitaclla_appmovil_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.CropCaresService
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Crop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CropCareActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val cropCaresService = CropCaresService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_care)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchCaresByCropId(1) // Example cropId
    }

    private fun fetchCaresByCropId(cropId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val caresList = cropCaresService.getCaresByCropId(cropId)
                withContext(Dispatchers.Main) {
                    recyclerView.adapter = CropCareAdapter(caresList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CropCareActivity, "Failed to load cares", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}