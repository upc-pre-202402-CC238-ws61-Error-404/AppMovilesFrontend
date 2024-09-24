// DiseasesActivity.kt
package com.example.chaquitaclla_appmovil_android

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.DiseaseService
import com.example.chaquitaclla_appmovil_android.crops_details.PestService
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.DiseaseAdapter
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.PestAdapter
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Disease
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Pest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiseasesActivity : AppCompatActivity() {

    private lateinit var diseaseService: DiseaseService
    private lateinit var pestService: PestService
    private lateinit var diseaseRecyclerView: RecyclerView
    private lateinit var pestRecyclerView: RecyclerView
    private lateinit var diseaseAdapter: DiseaseAdapter
    private lateinit var pestAdapter: PestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diseases)

        diseaseService = DiseaseService(this)
        pestService = PestService(this)
        diseaseRecyclerView = findViewById(R.id.diseaseRecyclerView)
        pestRecyclerView = findViewById(R.id.pestRecyclerView)
        diseaseRecyclerView.layoutManager = LinearLayoutManager(this)
        pestRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchDiseasesAndPests()
    }

    private fun fetchDiseasesAndPests() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val diseases = diseaseService.getDiseasesByCropId(1) // Replace with actual crop ID
                val pests = pestService.getPestsByCropId(1) // Replace with actual crop ID
                withContext(Dispatchers.Main) {
                    if (diseases.isNotEmpty()) {
                        diseaseAdapter = DiseaseAdapter(diseases)
                        diseaseRecyclerView.adapter = diseaseAdapter
                    } else {
                        Toast.makeText(this@DiseasesActivity, "No diseases found", Toast.LENGTH_LONG).show()
                    }
                    if (pests.isNotEmpty()) {
                        pestAdapter = PestAdapter(pests)
                        pestRecyclerView.adapter = pestAdapter
                    } else {
                        Toast.makeText(this@DiseasesActivity, "No pests found", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("DiseasesActivity", "Error fetching data", e)
                    Toast.makeText(this@DiseasesActivity, "Failed to fetch data", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}