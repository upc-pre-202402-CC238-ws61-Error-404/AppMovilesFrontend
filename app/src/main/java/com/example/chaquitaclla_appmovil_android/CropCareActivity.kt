package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CropCareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_care)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val popupTriggerTextView = findViewById<View>(R.id.popupTriggerTextView)
        popupTriggerTextView.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_crop_care -> {
                    startActivity(Intent(this, CropCareActivity::class.java))
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

        try {
            val fieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldPopup.isAccessible = true
            val menuPopupHelper = fieldPopup.get(popupMenu)
            val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
            val setPopupStyle = classPopupHelper.getMethod("setPopupStyle", Int::class.java)
            setPopupStyle.invoke(menuPopupHelper, R.style.PopupMenuStyle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popupMenu.show()
    }
}