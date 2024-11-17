package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.DiseaseService
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.DiseaseAdapter
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Disease
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class DiseasesActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private val diseaseService = DiseaseService()
    private var diseaseList: List<Disease> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_diseases, findViewById(R.id.container))
        enableEdgeToEdge()

        // Configurar el BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_home

        recyclerView = findViewById(R.id.diseaseRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DiseaseAdapter(diseaseList)

        val cropId = intent.getIntExtra("CROP_ID", -1)
        val sowingId = intent.getIntExtra("SOWING_ID", -1)
        if (cropId != -1) {
            fetchDiseasesByCropId(cropId)
        } else {
            Toast.makeText(this, "Invalid crop ID", Toast.LENGTH_SHORT).show()
        }

        setupSpinner()
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


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }
                view?.let {
                    val cropId = intent.getIntExtra("CROP_ID", -1)
                    val sowingId = intent.getIntExtra("SOWING_ID", -1)
                    when (position) {
                        0 -> startActivity(Intent(this@DiseasesActivity, GeneralCropInfo::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                            putExtra("CROP_ID", cropId)
                        })
                        1 -> startActivity(Intent(this@DiseasesActivity, CropCareActivity::class.java).apply{
                            putExtra("SOWING_ID", sowingId)
                            putExtra("CROP_ID", cropId)
                        })
                        2 -> startActivity(Intent(this@DiseasesActivity, ControlsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                            putExtra("CROP_ID", cropId)
                        })
                        3 -> startActivity(Intent(this@DiseasesActivity, DiseasesActivity::class.java).apply{
                            putExtra("SOWING_ID", sowingId)
                            putExtra("CROP_ID", cropId)
                        })
                        4 -> startActivity(Intent(this@DiseasesActivity, ProductsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                            putExtra("CROP_ID", cropId)
                        })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }
        val diseasesPosition = resources.getStringArray(R.array.crop_info_options).indexOf("Diseases or Pest")
        spinner.setSelection(diseasesPosition)
    }

    private fun fetchDiseasesByCropId(cropId: Int) {
        lifecycleScope.launch {
            try {
                diseaseList = diseaseService.getDiseasesByCropId(cropId)
                if (diseaseList.isNotEmpty()) {
                    recyclerView.adapter = DiseaseAdapter(diseaseList)
                } else {
                    Toast.makeText(this@DiseasesActivity, "No diseases found", Toast.LENGTH_LONG).show()
                }
            } catch (e: retrofit2.HttpException) {
                Log.e("DiseasesActivity", "HTTP Exception: ${e.message}", e)
                Toast.makeText(this@DiseasesActivity, "Server error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("DiseasesActivity", "Failed to load diseases", e)
                Toast.makeText(this@DiseasesActivity, "Failed to load diseases due to an error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}