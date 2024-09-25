package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import DB.AppDataBase
import com.example.chaquitaclla_appmovil_android.sowingsManagement.SowingsService
import java.text.SimpleDateFormat
import java.util.*

class GeneralCropInfo : BaseActivity() {
    private lateinit var appDB: AppDataBase
    private lateinit var sowingsService: SowingsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_general_crop_info, findViewById(R.id.container))
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appDB = AppDataBase.getDatabase(this)
        sowingsService = SowingsService()

        val sowingId = intent.getIntExtra("SOWING_ID", -1)
        Log.d("GeneralCropInfo", "Received sowing ID: $sowingId")
        if (sowingId != -1) {
            fetchSowingDetails(sowingId)
        }

        setupSpinner()
    }

    private fun fetchSowingDetails(sowingId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val sowing = appDB.sowingDAO().getSowingById(sowingId)
            val crop = sowing?.let { sowingsService.getCropById(it.cropId) }
            withContext(Dispatchers.Main) {
                sowing?.let {
                    Log.d("GeneralCropInfo", "Fetched sowing details: $it")
                    Log.d("GeneralCropInfo", "Sowing start date: ${it.startDate}")
                    val cropName = crop?.name ?: "Unknown"
                    val area = it.areaLand
                    val description = crop?.description ?: "No description available"

                    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = outputFormat.format(it.startDate)

                    findViewById<TextView>(R.id.right_text_1).text = cropName
                    findViewById<TextView>(R.id.right_text_2).text = formattedDate
                    findViewById<TextView>(R.id.right_text_3).text = "$area m²"
                    findViewById<TextView>(R.id.crop_description).text = description
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
                        0 -> startActivity(Intent(this@GeneralCropInfo, GeneralCropInfo::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                        1 -> startActivity(Intent(this@GeneralCropInfo, CropCareActivity::class.java))
                        2 -> startActivity(Intent(this@GeneralCropInfo, ControlsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                        3 -> startActivity(Intent(this@GeneralCropInfo, DiseasesActivity::class.java))
                        4 -> startActivity(Intent(this@GeneralCropInfo, ProductsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }
    }
}