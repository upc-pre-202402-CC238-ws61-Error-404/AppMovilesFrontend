package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.navigation_forum -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.navigation_profile -> {
                    // Respond to navigation item 3 click
                    true
                }
                R.id.navigation_history -> {
                    // Respond to navigation item 4 click
                    true
                }
                R.id.navigation_statistics -> {
                    val intent = Intent(this, StatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}