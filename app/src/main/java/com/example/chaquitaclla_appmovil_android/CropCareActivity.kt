package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.CropCaresService
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.CropCareAdapter
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Cares
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CropCareActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cropCareSpinner: Spinner
    private val cropCaresService = CropCaresService()
    private var caresList: List<Cares> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_care)

        Log.d("CropCareActivity", "onCreate called")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CropCareAdapter(caresList) // Set an empty adapter initially

        cropCareSpinner = findViewById(R.id.cropCareSpinner)
        setupSpinner()

        fetchCaresByCropId(1) // Example cropId
    }

    private fun setupSpinner() {
        Log.d("CropCareActivity", "Setting up spinner")
        val options = arrayOf("General Info", "Diseases", "Pests", "Care")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cropCareSpinner.adapter = adapter

        cropCareSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            private var isFirstSelection = true

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }
                Log.d("CropCareActivity", "Spinner item selected: $position")
                when (position) {
                    0 -> startActivity(Intent(this@CropCareActivity, GeneralCropInfo::class.java))
                    1 -> startActivity(Intent(this@CropCareActivity, DiseasesActivity::class.java))
                    2 -> startActivity(Intent(this@CropCareActivity, ProductsActivity::class.java))
                    3 -> fetchCaresByCropId(1) // Refresh current activity
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d("CropCareActivity", "Nothing selected in spinner")
            }
        }
    }

    private fun fetchCaresByCropId(cropId: Int) {
        Log.d("CropCareActivity", "Fetching cares by crop ID: $cropId")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                caresList = cropCaresService.getCaresByCropId(cropId)
                Log.d("CropCareActivity", "Cares fetched: ${caresList.size} items")
                withContext(Dispatchers.Main) {
                    if (caresList.isNotEmpty()) {
                        recyclerView.adapter = CropCareAdapter(caresList)
                        Log.d("CropCareActivity", "Adapter set with cares list")
                    } else {
                        Toast.makeText(this@CropCareActivity, "No cares found", Toast.LENGTH_LONG).show()
                        Log.d("CropCareActivity", "No cares found")
                    }
                }
            } catch (e: Exception) {
                Log.e("CropCareActivity", "Failed to load cares", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CropCareActivity, "Failed to load cares", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}