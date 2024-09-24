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
                    //TODO: Call intent to go to HomeActivity(Register Sowing)
                    true
                }
                R.id.navigation_forum -> {
                    //TODO:Call intent to go to ForumActivity
                    true
                }
                R.id.navigation_profile -> {
                    //TODO: Call intent to go to ProfileActivity
                    true
                }
                R.id.navigation_history -> {
                    //TODO: Call intent to go to HistoryActivity
                    true
                }
                R.id.navigation_statistics -> {
                    val intent = Intent(this, StatisticsActivity::class.java)
                    //Here we are using FLAG_ACTIVITY_REORDER_TO_FRONT to avoid creating a new instance of the activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}