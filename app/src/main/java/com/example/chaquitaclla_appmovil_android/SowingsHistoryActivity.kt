package com.example.chaquitaclla_appmovil_android

import DB.AppDataBase
import Entities.Sowing
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.chaquitaclla_appmovil_android.sowingsManagement.SowingsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class SowingsHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var database: AppDataBase
    private lateinit var sowingsService: SowingsService
    private var sowings: List<Sowing> = listOf()
    private var sowingsFiltered: List<Sowing> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sowings_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = AppDataBase.getDatabase(this)
        sowingsService = SowingsService()
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        fetchAndDisplaySowings()
    }

    private fun fetchAndDisplaySowings() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                sowings = database.sowingDAO().getAllSowings()
                sowingsFiltered = sowings
                withContext(Dispatchers.Main) {
                    displaySowings(sowingsFiltered)
                }
            } catch (e: Exception) {
                Log.e("SowingsHistoryActivity", "Error fetching sowings: ${e.message}")
            }
        }
    }

    private fun displaySowings(sowings: List<Sowing>) {
        recyclerView.adapter = object : RecyclerView.Adapter<SowingViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SowingViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sowing_history, parent, false)
                return SowingViewHolder(view)
            }

            override fun onBindViewHolder(holder: SowingViewHolder, position: Int) {
                val sowing = sowings[position]
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                holder.txtStartDate.text = "Start Date: ${dateFormat.format(sowing.startDate)}"
                holder.txtEndDate.text = "End Date: ${dateFormat.format(sowing.endDate)}"

                CoroutineScope(Dispatchers.IO).launch {
                    val crop = sowingsService.getCropById(sowing.cropId)
                    withContext(Dispatchers.Main) {
                        holder.txtCropName.text = crop?.name ?: "Unknown Crop"
                        Glide.with(holder.itemView.context).load(crop?.imageUrl).into(holder.imgCrop)
                    }
                }

                holder.btnSee.setOnClickListener {
                    showSowingDetailsDialog(sowing)
                }
            }

            override fun getItemCount(): Int {
                return sowings.size
            }
        }
    }

    private fun showSowingDetailsDialog(sowing: Sowing) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_sowing_details, null)
        val txtCropName: TextView = dialogView.findViewById(R.id.txtCropName)
        val imgCrop: ImageView = dialogView.findViewById(R.id.imgCrop)
        val txtStartDate: TextView = dialogView.findViewById(R.id.txtStartDate)
        val txtEndDate: TextView = dialogView.findViewById(R.id.txtEndDate)
        val txtDescription: TextView = dialogView.findViewById(R.id.txtDescription)
        val txtPhenologicalPhase: TextView = dialogView.findViewById(R.id.txtPhenologicalPhase)
        val btnClose: Button = dialogView.findViewById(R.id.btnClose)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        txtStartDate.text = "Start Date: ${dateFormat.format(sowing.startDate)}"
        txtEndDate.text = "End Date: ${dateFormat.format(sowing.endDate)}"
        txtPhenologicalPhase.text = "Phenological Phase: ${sowing.phenologicalPhaseName}"

        CoroutineScope(Dispatchers.IO).launch {
            val crop = sowingsService.getCropById(sowing.cropId)
            withContext(Dispatchers.Main) {
                txtCropName.text = crop?.name ?: "Unknown Crop"
                txtDescription.text = crop?.description ?: "No description available"
                Glide.with(this@SowingsHistoryActivity).load(crop?.imageUrl).into(imgCrop)
            }
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    class SowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCropName: TextView = itemView.findViewById(R.id.txtCropName)
        val imgCrop: ImageView = itemView.findViewById(R.id.imgCrop)
        val txtStartDate: TextView = itemView.findViewById(R.id.txtStartDate)
        val txtEndDate: TextView = itemView.findViewById(R.id.txtEndDate)
        val btnSee: Button = itemView.findViewById(R.id.btnSee)
    }
}