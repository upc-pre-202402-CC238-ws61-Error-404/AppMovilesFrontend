package com.example.chaquitaclla_appmovil_android.iam.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaquitaclla_appmovil_android.CountryCityData
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.SessionManager
import com.example.chaquitaclla_appmovil_android.iam.RetrofitClient
import com.example.chaquitaclla_appmovil_android.iam.beans.ProfileRequest
import com.example.chaquitaclla_appmovil_android.iam.services.ProfileServiceImpl

class CreateProfileActivity : AppCompatActivity() {

    private lateinit var spinnerCountry: Spinner
    private lateinit var spinnerCity: Spinner
    private lateinit var profileServiceImpl: ProfileServiceImpl

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rlCreateProfile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinnerCountry = findViewById(R.id.spinnerCountry)
        spinnerCity = findViewById(R.id.spinnerCity)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        profileServiceImpl = ProfileServiceImpl(RetrofitClient.profileService)

        setupCountrySpinner()

        btnSignUp.setOnClickListener {

            val firtName = findViewById<EditText>(R.id.edtName).text.toString()
            val lastName = findViewById<EditText>(R.id.edtLastName).text.toString()

            val selectedCountry = spinnerCountry.selectedItem.toString()
            val selectedCity = spinnerCity.selectedItem.toString()

            val countryId = CountryCityData.countries[selectedCountry]
            val cityId = CountryCityData.cities[countryId]?.indexOf(selectedCity)?.plus(1) // Asumiendo que las ciudades están indexadas desde 1

            if (countryId != null && cityId != null) {

                val email = SessionManager.username
                val token = SessionManager.token

                val title = intent.getStringExtra("title")
                val cost = intent.getStringExtra("cost")
                val text1 = intent.getStringExtra("text1")
                val text2 = intent.getStringExtra("text2")

                var subscriptionId: Int = 0

                if (title == "Basic") {
                    subscriptionId = 1
                } else if (title == "Regular") {
                    subscriptionId = 2
                } else if (title == "Premium") {
                    subscriptionId = 3
                }

                if (token != null) {
                    val profileRequest = ProfileRequest(firtName, lastName, email.toString(), subscriptionId, countryId, cityId)
                    profileServiceImpl.saveProfile(token, profileRequest) { profileResponse ->
                        if (profileResponse != null) {
                            Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, PayActivity::class.java).apply {
                                putExtra("title", title)
                                putExtra("cost", cost)
                                putExtra("text1", text1)
                                putExtra("text2", text2)
                            }
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Failed to save profile", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Token is missing", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please select a valid country and city", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupCountrySpinner() {
        val countryNames = CountryCityData.countries.keys.toList()
        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryNames)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = countryAdapter

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCountry = countryNames[position]
                val countryId = CountryCityData.countries[selectedCountry]
                setupCitySpinner(countryId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun setupCitySpinner(countryId: Int?) {
        countryId?.let {
            val cityNames = CountryCityData.cities[it] ?: emptyList()
            val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityNames)
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCity.adapter = cityAdapter
        }
    }
}