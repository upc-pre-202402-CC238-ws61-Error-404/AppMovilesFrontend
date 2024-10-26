package com.example.chaquitaclla_appmovil_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chaquitaclla_appmovil_android.forum.activitys.AnswersActivity
import com.example.chaquitaclla_appmovil_android.forum.activitys.ForumManagementActivity
import com.example.chaquitaclla_appmovil_android.iam.activitys.ProfileActivity
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
                    if (!isSowingsManagementActivity()) {
                        val intent = Intent(this, SowingsManagementActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }
                    true
                }
                R.id.navigation_forum -> {
                    if (!isForumActivity()) {
                        val intent = Intent(this, ForumManagementActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }
                    true
                }
                R.id.navigation_profile -> {
                    if (this !is ProfileActivity) {
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }
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

    private fun isSowingsManagementActivity(): Boolean {
        return this is SowingsManagementActivity || this is ControlsActivity || this is CropCareActivity || this is DiseasesActivity || this is ProductsActivity
                || this is GeneralCropInfo
    }

    private fun isForumActivity(): Boolean {
        return this is ForumManagementActivity || this is AnswersActivity
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        when (this) {
            is SowingsManagementActivity -> bottomNavigationView.selectedItemId = R.id.navigation_home
            is SowingsHistoryActivity -> bottomNavigationView.selectedItemId = R.id.navigation_history
            is StatisticsActivity -> bottomNavigationView.selectedItemId = R.id.navigation_statistics
            is ProfileActivity -> bottomNavigationView.selectedItemId = R.id.navigation_profile
            is ForumManagementActivity -> bottomNavigationView.selectedItemId = R.id.navigation_forum
        }
    }
}