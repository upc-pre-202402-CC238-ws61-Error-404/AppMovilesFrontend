// app/src/main/java/com/example/chaquitaclla_appmovil_android/DiseasesActivity.kt
package com.example.chaquitaclla_appmovil_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.DiseaseService
import com.example.chaquitaclla_appmovil_android.crops_details.PestService
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.PestAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiseasesActivity : AppCompatActivity() {
    private lateinit var diseaseRecyclerView: RecyclerView
    private lateinit var pestRecyclerView: RecyclerView
    private lateinit var diseaseService: DiseaseService
    private lateinit var pestService: PestService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diseases)

        diseaseRecyclerView = findViewById(R.id.diseaseRecyclerView)
        diseaseRecyclerView.layoutManager = LinearLayoutManager(this)

        pestRecyclerView = findViewById(R.id.pestRecyclerView)
        pestRecyclerView.layoutManager = LinearLayoutManager(this)

        diseaseService = DiseaseService(this)
        pestService = PestService(this)

        fetchDiseasesByCropId(1) // Example cropId
        fetchPestsByCropId(1) // Example cropId
    }

    private fun fetchDiseasesByCropId(cropId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val diseasesList = diseaseService.getDiseasesByCropId(cropId)
                withContext(Dispatchers.Main) {
                    diseaseRecyclerView.adapter = DiseaseAdapter(diseasesList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DiseasesActivity, "Failed to load diseases", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchPestsByCropId(cropId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pestsList = pestService.getPestsByCropId(cropId)
                withContext(Dispatchers.Main) {
                    pestRecyclerView.adapter = PestAdapter(pestsList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DiseasesActivity, "Failed to load pests", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}