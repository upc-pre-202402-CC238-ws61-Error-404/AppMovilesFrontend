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

        cropCareSpinner = findViewById(R.id.dropdown_menu)
        setupSpinner()

        val cropId = intent.getIntExtra("CROP_ID", 5)
        Log.d("CropCareActivity", "Received cropId from intent: $cropId")
        if (cropId != -1) {
            fetchCaresByCropId(cropId)
        } else {
            Log.e("CropCareActivity", "Invalid crop ID")
            Toast.makeText(this, "Invalid crop ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.dropdown_menu)
        ArrayAdapter.createFromResource(
            this,
            R.array.crop_info_options,
            R.layout.spinner_item_white_text
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item_white_text)
            spinner.adapter = adapter
        }

        var isFirstSelection = true

        // CropCareActivity.kt
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }
                view?.let {
                    val sowingId = intent.getIntExtra("SOWING_ID", 7)
                    Log.d("CropCareActivity", "Spinner item selected, sowingId: $sowingId")
                    when (position) {
                        0 -> startActivity(Intent(this@CropCareActivity, GeneralCropInfo::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                        1 -> startActivity(Intent(this@CropCareActivity, CropCareActivity::class.java))
                        2 -> startActivity(Intent(this@CropCareActivity, ControlsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                        3 -> startActivity(Intent(this@CropCareActivity, DiseasesActivity::class.java))
                        4 -> startActivity(Intent(this@CropCareActivity, ProductsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }

        // Set the selected item to "Crop Care" if in CropCareActivity
        val cropCarePosition = resources.getStringArray(R.array.crop_info_options).indexOf("Crop Care")
        spinner.setSelection(cropCarePosition)
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