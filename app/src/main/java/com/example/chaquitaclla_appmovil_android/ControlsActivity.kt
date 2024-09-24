// ControlsActivity.kt
package com.example.chaquitaclla_appmovil_android

import DB.AppDataBase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.ControlAdapter
import Entities.Control
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ControlsActivity : AppCompatActivity() {
    private lateinit var controlRecyclerView: RecyclerView
    private lateinit var controlAdapter: ControlAdapter
    private lateinit var appDB: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controls)

        controlRecyclerView = findViewById(R.id.controlRecyclerView)
        controlRecyclerView.layoutManager = LinearLayoutManager(this)

        val cropInfoSpinner: Spinner = findViewById(R.id.cropInfoSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.crop_info_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cropInfoSpinner.adapter = adapter
        }

        appDB = AppDataBase.getDatabase(this)

        val sowingId = intent.getIntExtra("SOWING_ID", -1)
        Log.d("ControlsActivity", "Received sowingId: $sowingId")
        if (sowingId != -1) {
            fetchControlsBySowingId(sowingId)
        } else {
            Log.e("ControlsActivity", "Invalid sowing ID")
            Toast.makeText(this, "Invalid sowing ID", Toast.LENGTH_SHORT).show()
        }

        val addButton: Button = findViewById(R.id.addControlButton)
        addButton.setOnClickListener {
            showAddControlDialog()
        }
    }

    private fun fetchControlsBySowingId(sowingId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val controls = appDB.controlDAO().getControlsBySowingId(sowingId)
                withContext(Dispatchers.Main) {
                    controlAdapter = ControlAdapter(controls, ::onEditClick, ::onDeleteClick)
                    controlRecyclerView.adapter = controlAdapter
                }
            } catch (e: Exception) {
                Log.e("ControlsActivity", "Error fetching controls: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ControlsActivity, "Error fetching controls: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showAddControlDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_control, null)
        val sowingConditionEditText = dialogView.findViewById<EditText>(R.id.edittext_sowing_condition)
        val stemConditionEditText = dialogView.findViewById<EditText>(R.id.edittext_stem_condition)
        val soilMoistureEditText = dialogView.findViewById<EditText>(R.id.edittext_soil_moisture)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add Control")
            .setPositiveButton("Add") { _, _ ->
                val sowingId = intent.getIntExtra("SOWING_ID", -1)
                val sowingCondition = sowingConditionEditText.text.toString()
                val stemCondition = stemConditionEditText.text.toString()
                val soilMoisture = soilMoistureEditText.text.toString()
                addControl(sowingId, sowingCondition, stemCondition, soilMoisture)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun addControl(sowingId: Int, sowingCondition: String, stemCondition: String, soilMoisture: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newControl = Control(
                    id = 0, // 0 because it will be auto-generated
                    sowingId = sowingId,
                    sowingCondition = sowingCondition,
                    stemCondition = stemCondition,
                    sowingSoilMoisture = soilMoisture
                )
                appDB.controlDAO().insertControl(newControl)
                fetchControlsBySowingId(sowingId) // Refresh the list
            } catch (e: Exception) {
                Log.e("ControlsActivity", "Error adding control: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ControlsActivity, "Error adding control: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onEditClick(control: Control) {
        // Handle edit action
        Toast.makeText(this, "Edit ${control.sowingCondition}", Toast.LENGTH_SHORT).show()
    }

    private fun onDeleteClick(control: Control) {
        // Handle delete action
        Toast.makeText(this, "Delete ${control.sowingCondition}", Toast.LENGTH_SHORT).show()
    }
}