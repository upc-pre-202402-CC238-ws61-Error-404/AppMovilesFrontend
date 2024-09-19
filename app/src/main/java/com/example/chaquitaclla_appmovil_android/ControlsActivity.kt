package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ControlsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controls)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val popupTriggerTextView = findViewById<TextView>(R.id.popupTriggerTextView)
        popupTriggerTextView.setOnClickListener {
            showPopup(it)
        }
    }

    private fun showPopup(anchorView: View) {
        val popupMenu = PopupMenu(this, anchorView)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_crop_care -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.menu_controls -> {
                    startActivity(Intent(this, ControlsActivity::class.java))
                    true
                }
                R.id.menu_diseases -> {
                    startActivity(Intent(this, DiseasesActivity::class.java))
                    true
                }
                R.id.menu_products -> {
                    startActivity(Intent(this, ProductsActivity::class.java))
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}