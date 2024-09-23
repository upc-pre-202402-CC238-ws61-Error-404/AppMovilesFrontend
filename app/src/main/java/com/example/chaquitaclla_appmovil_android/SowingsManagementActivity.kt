// SowingsManagementActivity.kt
package com.example.chaquitaclla_appmovil_android.sowingsManagement

import DB.AppDataBase
import Entities.Sowing
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.chaquitaclla_appmovil_android.GeneralCropInfo
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.Crop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class SowingsManagementActivity : AppCompatActivity() {

    private lateinit var sowingsService: SowingsService
    private lateinit var sowingsContainer: LinearLayout
    private lateinit var addCropButton: Button
    private lateinit var crops: List<Crop>
    private lateinit var appDB: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sowings_management)

        sowingsService = SowingsService()
        sowingsContainer = findViewById(R.id.sowings_container)
        addCropButton = findViewById(R.id.button_add_crop)

        appDB = AppDataBase.getDatabase(this)

        fetchAndDisplaySowings()

        addCropButton.setOnClickListener {
            showAddSowingDialog()
        }
    }

    private fun fetchAndDisplaySowings() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sowings = appDB.sowingDAO().getAllSowings()
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
        sowingsContainer.removeAllViews()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sowings.forEach { sowing ->
            val cardView = LayoutInflater.from(this).inflate(R.layout.item_sowing, sowingsContainer, false)

            val imgCrop = cardView.findViewById<ImageView>(R.id.imgCrop)
            val txtCropName = cardView.findViewById<TextView>(R.id.txtCropName)
            val txtPhenologicalPhaseName = cardView.findViewById<TextView>(R.id.txtPhenologicalPhaseName)
            val txtStartDate = cardView.findViewById<TextView>(R.id.txtStartDate)
            val txtEndDate = cardView.findViewById<TextView>(R.id.txtEndDate)
            val txtArea = cardView.findViewById<TextView>(R.id.txtArea)
            val imgTrashIcon = cardView.findViewById<ImageView>(R.id.imgTrashIcon)
            val imgViewIcon = cardView.findViewById<ImageView>(R.id.imgViewIcon)

            val crop = cropMap[sowing.cropId]
            if (crop != null) {
                Glide.with(this).load(crop.imageUrl).into(imgCrop)
                txtCropName.text = crop.name
            } else {
                imgCrop.setImageResource(R.drawable.ic_launcher_background)
                txtCropName.text = "Unknown"
            }

            txtPhenologicalPhaseName.text = sowing.phenologicalPhaseName
            txtStartDate.text = dateFormat.format(sowing.startDate)
            txtEndDate.text = dateFormat.format(sowing.endDate)
            txtArea.text = "${sowing.areaLand} m²"

            imgViewIcon.setOnClickListener {
                Log.d("SowingsManagement", "Viewing details for sowing ID: ${sowing.id}")
                val intent = Intent(this, GeneralCropInfo::class.java).apply {
                    putExtra("SOWING_ID", sowing.id)
                }
                startActivity(intent)
            }

            sowingsContainer.addView(cardView)
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
                val startDate = Date()
                val calendar = Calendar.getInstance().apply {
                    time = startDate
                    add(Calendar.MONTH, 6)
                }
                val endDate = calendar.time

                val newSowing = Sowing(
                    id = 0,
                    startDate = startDate,
                    endDate = endDate,
                    areaLand = area,
                    status = false,
                    phenologicalPhase = 0,
                    cropId = cropId,
                    phenologicalPhaseName = "Germination",
                    favourite = false
                )
                appDB.sowingDAO().insertSowing(newSowing)
                fetchAndDisplaySowings()
            } catch (e: Exception) {
                Log.e("SowingsManagement", "Error adding sowing: ${e.message}")
            }
        }
    }
}