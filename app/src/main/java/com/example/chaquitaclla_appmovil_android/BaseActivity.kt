package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chaquitaclla_appmovil_android.sowingsManagement.SowingsManagementActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (this !is SowingsManagementActivity) {
                        val intent = Intent(this, SowingsManagementActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }
                    true
                }
                R.id.navigation_forum -> {
                    // TODO: Call intent to go to ForumActivity
                    true
                }
                R.id.navigation_profile -> {
                    // TODO: Call intent to go to ProfileActivity
                    true
                }
                R.id.navigation_history -> {
                    if (this !is SowingsHistoryActivity) {
                        val intent = Intent(this, SowingsHistoryActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }
                    true
                }
                R.id.navigation_statistics -> {
                    if (this !is StatisticsActivity) {
                        val intent = Intent(this, StatisticsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        when (this) {
            is SowingsManagementActivity -> bottomNavigationView.selectedItemId = R.id.navigation_home
            is SowingsHistoryActivity -> bottomNavigationView.selectedItemId = R.id.navigation_history
            is StatisticsActivity -> bottomNavigationView.selectedItemId = R.id.navigation_statistics
            //TODO: Add cases for ForumActivity and ProfileActivity
        }
    }
}