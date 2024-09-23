// src/main/java/com/example/chaquitaclla_appmovil_android/PlansActivity.kt
package com.example.chaquitaclla_appmovil_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class PlansActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plans)

        val cardButtonB: MaterialCardView = findViewById(R.id.cardButtonB)
        val cardButtonR: MaterialCardView = findViewById(R.id.cardButtonR)
        val cardButtonP: MaterialCardView = findViewById(R.id.cardButtonP)

        cardButtonB.setOnClickListener {
            val titleB = findViewById<TextView>(R.id.cardTitleB).text.toString()
            val costB = findViewById<TextView>(R.id.cardCostB).text.toString()
            val text1B = findViewById<TextView>(R.id.cardText1B).text.toString()
            val text2B = findViewById<TextView>(R.id.cardText2B).text.toString()

            val intent = Intent(this, PayActivity::class.java).apply {
                putExtra("title", titleB)
                putExtra("cost", costB)
                putExtra("text1", text1B)
                putExtra("text2", text2B)
            }
            startActivity(intent)
        }
        cardButtonR.setOnClickListener {
            val titleR = findViewById<TextView>(R.id.cardTitleR).text.toString()
            val costR = findViewById<TextView>(R.id.cardCostR).text.toString()
            val text1R = findViewById<TextView>(R.id.cardText1R).text.toString()
            val text2R = findViewById<TextView>(R.id.cardText2R).text.toString()

            val intent = Intent(this, PayActivity::class.java).apply {
                putExtra("title", titleR)
                putExtra("cost", costR)
                putExtra("text1", text1R)
                putExtra("text2", text2R)
            }
            startActivity(intent)
        }
        cardButtonP.setOnClickListener {
            val titleP = findViewById<TextView>(R.id.cardTitleP).text.toString()
            val costP = findViewById<TextView>(R.id.cardCostP).text.toString()
            val text1P = findViewById<TextView>(R.id.cardText1P).text.toString()
            val text2P = findViewById<TextView>(R.id.cardText2P).text.toString()

            val intent = Intent(this, PayActivity::class.java).apply {
                putExtra("title", titleP)
                putExtra("cost", costP)
                putExtra("text1", text1P)
                putExtra("text2", text2P)
            }
            startActivity(intent)
        }
    }
}