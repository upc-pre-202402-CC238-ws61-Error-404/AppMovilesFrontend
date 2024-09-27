package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.DiseaseService
import com.example.chaquitaclla_appmovil_android.crops_details.PestService
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.DiseaseAdapter
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.PestAdapter
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Disease
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Pest
import com.example.chaquitaclla_appmovil_android.databinding.ActivityDiseasesBinding
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
        setupSearch()

        val cropId = intent.getIntExtra("CROP_ID", 5)
        val sowingId = intent.getIntExtra("SOWING_ID", 1)
        Log.d("DiseasesActivity", "Received cropId from intent: $cropId")
        Log.d("DiseasesActivity", "Received sowingId from intent: $sowingId")
        if (cropId != -1) {
            fetchDiseasesAndPests(cropId)
        } else {
            Log.e("DiseasesActivity", "Invalid crop ID")
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

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }
                view?.let {
                    val sowingId = intent.getIntExtra("SOWING_ID", 1)
                    Log.d("CropCareActivity", "Spinner item selected, sowingId: $sowingId")
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

        val cropCarePosition = resources.getStringArray(R.array.crop_info_options).indexOf("Diseases or Pest")
        spinner.setSelection(cropCarePosition)
    }

    private fun setupSearch() {
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (::diseaseAdapter.isInitialized) {
                    diseaseAdapter.filter(query)
                }
                if (::pestAdapter.isInitialized) {
                    pestAdapter.filter(query)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString()
                if (::diseaseAdapter.isInitialized) {
                    diseaseAdapter.filter(query)
                }
                true
            } else {
                false
            }
        }
    }

    private fun fetchDiseasesAndPests(cropId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val diseases = diseaseService.getDiseasesByCropId(cropId)
                val pests = pestService.getPestsByCropId(cropId)
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