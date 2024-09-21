package com.example.chaquitaclla_appmovil_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : BaseActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_main, findViewById(R.id.container))
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the button to navigate to StatisticsActivity
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            startActivity(newIntent(this))
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, StatisticsActivity::class.java)
        }
    }
}