package com.example.chaquitaclla_appmovil_android.sowingsManagement

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.Sowing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SowingsManagementActivity : AppCompatActivity() {

    private lateinit var sowingsService: SowingsService
    private lateinit var sowingsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sowings_management)

        sowingsService = SowingsService()
        sowingsContainer = findViewById(R.id.sowings_container)

        fetchAndDisplaySowings()
    }

    private fun fetchAndDisplaySowings() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sowings = sowingsService.getAllSowings()
                withContext(Dispatchers.Main) {
                    displaySowings(sowings)
                }
            } catch (e: Exception) {
                Log.e("SowingsManagement", "Error fetching sowings: ${e.message}")
            }
        }
    }

    private fun displaySowings(sowings: List<Sowing>) {
        sowings.forEach { sowing ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 8, 8, 8)
                setBackgroundResource(android.R.color.white)
                elevation = 4f
            }

            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                setImageResource(R.drawable.ic_launcher_background)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            val textContainer = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(8, 8, 8, 8)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
            }

            val textView = TextView(this).apply {
                text = sowing.phenologicalPhaseName
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black))
            }

            textContainer.addView(textView)
            card.addView(imageView)
            card.addView(textContainer)
            sowingsContainer.addView(card)
        }
    }
}