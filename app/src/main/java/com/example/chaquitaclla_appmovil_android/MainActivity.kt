package com.example.chaquitaclla_appmovil_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajustar el padding para los system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón para ir a StatisticsActivity
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            startActivity(newIntent(this))
        }

        // Botón para ir a SowingsManagementActivity
        val buttonSowings: Button = findViewById(R.id.button_sowings)
        buttonSowings.setOnClickListener {
            startActivity(newIntentSowings(this))
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, StatisticsActivity::class.java)
        }

        // Nueva función para ir a SowingsManagementActivity
        fun newIntentSowings(context: Context): Intent {
            return Intent(context, SowingsManagementActivity::class.java)
        }
    }
}
