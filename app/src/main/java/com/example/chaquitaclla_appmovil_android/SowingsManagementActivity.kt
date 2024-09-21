// SowingsManagementActivity.kt
package com.example.chaquitaclla_appmovil_android.sowingsManagement

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.Crop
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.Sowing
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.SowingDos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SowingsManagementActivity : AppCompatActivity() {

    private lateinit var sowingsService: SowingsService
    private lateinit var sowingsContainer: LinearLayout
    private lateinit var addCropButton: Button
    private lateinit var crops: List<Crop>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sowings_management)

        sowingsService = SowingsService()
        sowingsContainer = findViewById(R.id.sowings_container)
        addCropButton = findViewById(R.id.button_add_crop)

        fetchAndDisplaySowings()

        addCropButton.setOnClickListener {
            showAddSowingDialog()
        }
    }

    private fun fetchAndDisplaySowings() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sowings = sowingsService.getAllSowings()
                crops = sowingsService.getAllCrops()
                val cropMap = crops.associateBy { it.id }
                withContext(Dispatchers.Main) {
                    displaySowings(sowings, cropMap)
                }
            } catch (e: Exception) {
                Log.e("SowingsManagement", "Error fetching sowings: ${e.message}")
            }
        }
    }

    private fun displaySowings(sowings: List<Sowing>, cropMap: Map<Int, Crop>) {
        sowingsContainer.removeAllViews() // Clear the container before adding new views
        sowings.forEach { sowing ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 8, 8, 8)
                setBackgroundResource(android.R.color.white)
                elevation = 4f
            }

            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                val crop = cropMap[sowing.cropId]
                if (crop != null) {
                    Glide.with(this@SowingsManagementActivity)
                        .load(crop.imageUrl)
                        .into(this)
                } else {
                    setImageResource(R.drawable.ic_launcher_background)
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            val textContainer = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(8, 8, 8, 8)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
            }

            val cropName = TextView(this).apply {
                text = "Crop Name: ${cropMap[sowing.cropId]?.name ?: "Unknown"}"
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black))
            }

            val startDate = TextView(this).apply {
                text = "Start Date: ${sowing.startDate}"
                textSize = 14f
            }

            val endDate = TextView(this).apply {
                text = "End Date: ${sowing.endDate}"
                textSize = 14f
            }

            val area = TextView(this).apply {
                text = "Area: ${sowing.areaLand} m²"
                textSize = 14f
            }

            val trashIcon = ImageView(this).apply {
                setImageResource(android.R.drawable.ic_delete)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 16, 0, 0)
                }
            }

            textContainer.addView(cropName)
            textContainer.addView(startDate)
            textContainer.addView(endDate)
            textContainer.addView(area)
            textContainer.addView(trashIcon)
            card.addView(imageView)
            card.addView(textContainer)
            sowingsContainer.addView(card)
        }
    }

    private fun showAddSowingDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_sowing, null)
        val cropSpinner: Spinner = dialogView.findViewById(R.id.spinner_crop_names)
        val areaEditText: EditText = dialogView.findViewById(R.id.edittext_area)
        val cancelButton: Button = dialogView.findViewById(R.id.button_cancel)
        val addButton: Button = dialogView.findViewById(R.id.button_add)

        val cropNames = crops.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cropNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cropSpinner.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        addButton.setOnClickListener {
            val selectedCropName = cropSpinner.selectedItem.toString()
            val selectedCrop = crops.find { it.name == selectedCropName }
            val area = areaEditText.text.toString().toIntOrNull()

            if (selectedCrop != null && area != null) {
                addSowing(selectedCrop.id, area)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please select a crop and enter a valid area", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun addSowing(cropId: Int, area: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newSowing = SowingDos(cropId = cropId, areaLand = area)
                sowingsService.addSowing(newSowing)
                fetchAndDisplaySowings()
            } catch (e: Exception) {
                Log.e("SowingsManagement", "Error adding sowing: ${e.message}")
            }
        }
    }
}