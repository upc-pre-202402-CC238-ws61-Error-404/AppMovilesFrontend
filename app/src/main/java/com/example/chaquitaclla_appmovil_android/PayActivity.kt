package com.example.chaquitaclla_appmovil_android

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class PayActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        // Recibe los datos del Intent
        val title = intent.getStringExtra("title")
        val cost = intent.getStringExtra("cost")
        val text1 = intent.getStringExtra("text1")
        val text2 = intent.getStringExtra("text2")

        // Muestra los datos en la interfaz de usuario
        findViewById<TextView>(R.id.payTitle).text = title
        findViewById<TextView>(R.id.payCost).text = cost
        findViewById<TextView>(R.id.payText1).text = text1
        findViewById<TextView>(R.id.payText2).text = text2

        // Obtén la referencia al MaterialCardView
        val cardButton: MaterialCardView = findViewById(R.id.cardButton)

        // Cambia el backgroundTint del cardButton si el título es "Basic"
        if (title == "Basic") {
            cardButton.setCardBackgroundColor(resources.getColor(R.color.basic))
        } else if (title == "Regular") {
            cardButton.setCardBackgroundColor(resources.getColor(R.color.regular))
        } else if (title == "Premium") {
            cardButton.setCardBackgroundColor(resources.getColor(R.color.premium))
        }
    }
}