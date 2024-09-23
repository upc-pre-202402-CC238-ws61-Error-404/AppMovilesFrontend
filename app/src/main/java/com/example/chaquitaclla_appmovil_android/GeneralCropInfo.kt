// GeneralCropInfo.kt
package com.example.chaquitaclla_appmovil_android

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import DB.AppDataBase
import com.example.chaquitaclla_appmovil_android.sowingsManagement.SowingsService
import java.text.SimpleDateFormat
import java.util.*

class GeneralCropInfo : AppCompatActivity() {
    private lateinit var appDB: AppDataBase
    private lateinit var sowingsService: SowingsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_general_crop_info)
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

                    val spinner: Spinner = findViewById(R.id.dropdown_menu)
                    ArrayAdapter.createFromResource(
                        this@GeneralCropInfo,
                        R.array.crop_info_options,
                        R.layout.spinner_item_white_text
                    ).also { adapter ->
                        adapter.setDropDownViewResource(R.layout.spinner_item_white_text)
                        spinner.adapter = adapter
                    }
                    spinner.setSelection(0)
                }
            }
        }
    }
}