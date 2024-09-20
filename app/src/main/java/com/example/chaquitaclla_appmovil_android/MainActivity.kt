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
        // Botón para ir a LogInActivity
        val button: Button = findViewById(R.id.buttonLogin)
        button.setOnClickListener {
            startActivity(goLogin(this))
        }

        // Botón para ir a SignUpActivity
        val buttonSignUp: Button = findViewById(R.id.buttonRegister)
        buttonSignUp.setOnClickListener {
            startActivity(GoSignup(this))
        }
    }

    companion object {
        fun goLogin(context: Context): Intent {
            return Intent(context, LogInActivity::class.java)
        }

        fun GoSignup(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}