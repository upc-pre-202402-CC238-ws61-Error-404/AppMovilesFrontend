// app/src/main/java/com/example/chaquitaclla_appmovil_android/ControlsActivity.kt
package com.example.chaquitaclla_appmovil_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.ControlService
import com.example.chaquitaclla_appmovil_android.crops_details.adapters.ControlAdapter
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Controls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ControlsActivity : AppCompatActivity() {
    private lateinit var controlRecyclerView: RecyclerView
    private lateinit var controlService: ControlService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controls)

        controlRecyclerView = findViewById(R.id.controlRecyclerView)
        controlRecyclerView.layoutManager = LinearLayoutManager(this)

        controlService = ControlService()

        fetchControlsBySowingId(1) // Example sowingId
    }

    private fun fetchControlsBySowingId(sowingId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val controlsList = controlService.getControlsBySowingId(sowingId)
                withContext(Dispatchers.Main) {
                    controlRecyclerView.adapter = ControlAdapter(controlsList, ::onEditClick, ::onDeleteClick)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ControlsActivity, "Failed to load controls", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onEditClick(control: Controls) {
        // Handle edit action
        Toast.makeText(this, "Edit ${control.condition}", Toast.LENGTH_SHORT).show()
    }

    private fun onDeleteClick(control: Controls) {
        // Handle delete action
        Toast.makeText(this, "Delete ${control.condition}", Toast.LENGTH_SHORT).show()
    }
}