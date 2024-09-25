package com.example.chaquitaclla_appmovil_android

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.chaquitaclla_appmovil_android.iam.RetrofitClient
import com.example.chaquitaclla_appmovil_android.iam.beans.ProfileResponse
import com.example.chaquitaclla_appmovil_android.iam.services.ProfileServiceImpl

class ProfileActivity : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtCountry: EditText
    private lateinit var edtCity: EditText
    private lateinit var edtPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtCountry = findViewById(R.id.edtCountry)
        edtCity = findViewById(R.id.edtCity)
        edtPassword = findViewById(R.id.edtPassword)


        val token = SessionManager.token ?: return
        val profileServiceImpl = ProfileServiceImpl(RetrofitClient.profileService)

        profileServiceImpl.getAllProfiles(token) { profiles ->
            profiles?.let {
                val profile = it.find { profile -> profile.email == SessionManager.username }
                profile?.let { updateUI(it) }
            }
        }
    }

    private fun updateUI(profile: ProfileResponse) {
        edtName.setText(profile.fullName)
        edtEmail.setText(profile.email)
        edtCountry.setText(CountryCityData.countries.entries.find { it.value == profile.countryId }?.key)

        val cityList = CountryCityData.cities[profile.countryId]
        val cityName = cityList?.get(profile.cityId - 1) // Ajusta el índice según sea necesario
        edtCity.setText(cityName)

        // Assuming you have a way to get the password, otherwise remove this line
        edtPassword.setText("********") // Placeholder for password
    }
}