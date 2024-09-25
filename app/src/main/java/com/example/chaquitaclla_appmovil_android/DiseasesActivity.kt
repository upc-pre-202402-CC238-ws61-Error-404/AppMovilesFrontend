// DiseasesActivity.kt
package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.DiseaseService
import com.example.chaquitaclla_appmovil_android.crops_details.PestService
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.PestAdapter
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.DiseaseAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiseasesActivity : BaseActivity() {

    private lateinit var diseaseService: DiseaseService
    private lateinit var pestService: PestService
    private lateinit var diseaseRecyclerView: RecyclerView
    private lateinit var pestRecyclerView: RecyclerView
    private lateinit var diseaseAdapter: DiseaseAdapter
    private lateinit var pestAdapter: PestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_diseases, findViewById(R.id.container))
        enableEdgeToEdge()

        diseaseService = DiseaseService(this)
        pestService = PestService(this)
        diseaseRecyclerView = findViewById(R.id.diseaseRecyclerView)
        pestRecyclerView = findViewById(R.id.pestRecyclerView)
        diseaseRecyclerView.layoutManager = LinearLayoutManager(this)
        pestRecyclerView.layoutManager = LinearLayoutManager(this)

        setupSpinner()

        fetchDiseasesAndPests()
    }

    private fun fetchDiseasesAndPests() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val diseases = diseaseService.getDiseasesByCropId(5) // Replace with actual crop ID
                val pests = pestService.getPestsByCropId(5) // Replace with actual crop ID
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
                    val sowingId = intent.getIntExtra("SOWING_ID", -1)
                    when (position) {
                        0 -> startActivity(Intent(this@DiseasesActivity, GeneralCropInfo::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                        1 -> startActivity(Intent(this@DiseasesActivity, CropCareActivity::class.java))
                        2 -> startActivity(Intent(this@DiseasesActivity, ControlsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                        3 -> startActivity(Intent(this@DiseasesActivity, DiseasesActivity::class.java))
                        4 -> startActivity(Intent(this@DiseasesActivity, ProductsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }

        val diseasePosition = resources.getStringArray(R.array.crop_info_options).indexOf("Diseases")
        spinner.setSelection(diseasePosition)
    }
}