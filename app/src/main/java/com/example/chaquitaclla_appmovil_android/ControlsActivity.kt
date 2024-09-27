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
import android.content.Intent
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import com.example.chaquitaclla_appmovil_android.crops_details.SowingCondition
import com.example.chaquitaclla_appmovil_android.crops_details.SowingSoilMoisture
import com.example.chaquitaclla_appmovil_android.crops_details.StemCondition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class ControlsActivity : BaseActivity() {
    private lateinit var controlRecyclerView: RecyclerView
    private lateinit var controlAdapter: ControlAdapter
    private lateinit var appDB: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_controls, findViewById(R.id.container))
        enableEdgeToEdge()

        controlRecyclerView = findViewById(R.id.controlRecyclerView)
        controlRecyclerView.layoutManager = LinearLayoutManager(this)

        appDB = AppDataBase.getDatabase(this)

        val sowingId = intent.getIntExtra("SOWING_ID", 1)
        Log.d("ControlsActivity", "Received sowingId: $sowingId")
        if (sowingId != -1) {
            fetchControlsBySowingId(sowingId)
        } else {
            Log.e("ControlsActivity", "Invalid sowing ID")
            Toast.makeText(this, "Invalid sowing ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.addControlButton).setOnClickListener {
            showAddControlDialog()
        }

        setupSpinner()
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
        val sowingConditionSpinner = dialogView.findViewById<Spinner>(R.id.spinner_sowing_condition)
        val stemConditionSpinner = dialogView.findViewById<Spinner>(R.id.spinner_stem_condition)
        val soilMoistureSpinner = dialogView.findViewById<Spinner>(R.id.spinner_soil_moisture)
        val addControlButton = dialogView.findViewById<Button>(R.id.button_add)
        val cancelButton = dialogView.findViewById<Button>(R.id.button_cancel)

        val conditions = SowingCondition.entries.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conditions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sowingConditionSpinner.adapter = adapter
        stemConditionSpinner.adapter = adapter
        soilMoistureSpinner.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add Control")
            .create()

        addControlButton.setOnClickListener {
            val sowingId = intent.getIntExtra("SOWING_ID", 1)
            val sowingCondition = SowingCondition.valueOf(sowingConditionSpinner.selectedItem.toString())
            val stemCondition = StemCondition.valueOf(stemConditionSpinner.selectedItem.toString())
            val soilMoisture = SowingSoilMoisture.valueOf(soilMoistureSpinner.selectedItem.toString())
            val date = Date()
            addControl(sowingId, sowingCondition.toString(),
                stemCondition.toString(), soilMoisture.toString(), date)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun addControl(sowingId: Int, sowingCondition: String, stemCondition: String, soilMoisture: String, date: Date) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newControl = Control(
                    id = 0,
                    sowingId = sowingId,
                    sowingCondition = sowingCondition,
                    stemCondition = stemCondition,
                    sowingSoilMoisture = soilMoisture,
                    date = date
                )
                appDB.controlDAO().insertControl(newControl)
                fetchControlsBySowingId(sowingId)
            } catch (e: Exception) {
                Log.e("ControlsActivity", "Error adding control: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ControlsActivity, "Error adding control: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onEditClick(control: Control) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_control, null)
        val sowingConditionSpinner = dialogView.findViewById<Spinner>(R.id.spinner_sowing_condition)
        val stemConditionSpinner = dialogView.findViewById<Spinner>(R.id.spinner_stem_condition)
        val soilMoistureSpinner = dialogView.findViewById<Spinner>(R.id.spinner_soil_moisture)
        val addControlButton = dialogView.findViewById<Button>(R.id.button_add)
        val cancelButton = dialogView.findViewById<Button>(R.id.button_cancel)

        val conditions = SowingCondition.entries.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conditions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sowingConditionSpinner.adapter = adapter
        stemConditionSpinner.adapter = adapter
        soilMoistureSpinner.adapter = adapter

        sowingConditionSpinner.setSelection(conditions.indexOf(control.sowingCondition))
        stemConditionSpinner.setSelection(conditions.indexOf(control.stemCondition))
        soilMoistureSpinner.setSelection(conditions.indexOf(control.sowingSoilMoisture))

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Edit Control")
            .create()

        addControlButton.setOnClickListener {
            control.sowingCondition = sowingConditionSpinner.selectedItem.toString()
            control.stemCondition = stemConditionSpinner.selectedItem.toString()
            control.sowingSoilMoisture = soilMoistureSpinner.selectedItem.toString()
            updateControl(control)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateControl(control: Control) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appDB.controlDAO().updateControl(control)
                fetchControlsBySowingId(control.sowingId)
            } catch (e: Exception) {
                Log.e("ControlsActivity", "Error updating control: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ControlsActivity, "Error updating control: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onDeleteClick(control: Control) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appDB.controlDAO().deleteControl(control.id)
                fetchControlsBySowingId(control.sowingId)
            } catch (e: Exception) {
                Log.e("ControlsActivity", "Error deleting control: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ControlsActivity, "Error deleting control: ${e.message}", Toast.LENGTH_SHORT).show()
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
                        0 -> startActivity(Intent(this@ControlsActivity, GeneralCropInfo::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                        1 -> startActivity(Intent(this@ControlsActivity, CropCareActivity::class.java))
                        2 -> startActivity(Intent(this@ControlsActivity, ControlsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                        3 -> startActivity(Intent(this@ControlsActivity, DiseasesActivity::class.java))
                        4 -> startActivity(Intent(this@ControlsActivity, ProductsActivity::class.java).apply {
                            putExtra("SOWING_ID", sowingId)
                        })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }

        val controlPosition = resources.getStringArray(R.array.crop_info_options).indexOf("Controls")
        spinner.setSelection(controlPosition)
    }
}