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
import androidx.appcompat.app.AppCompatActivity
import com.example.chaquitaclla_appmovil_android.BaseActivity
import com.example.chaquitaclla_appmovil_android.CountryCityData
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.SessionManager
import com.example.chaquitaclla_appmovil_android.iam.RetrofitClient
import com.example.chaquitaclla_appmovil_android.iam.beans.ProfileRequestUpdate
import com.example.chaquitaclla_appmovil_android.iam.beans.ProfileResponse
import com.example.chaquitaclla_appmovil_android.iam.services.ProfileServiceImpl
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : BaseActivity() {
    private lateinit var edtName: EditText
    private lateinit var spinnerCountry: Spinner
    private lateinit var spinnerCity: Spinner
    private lateinit var edtPassword: EditText
    private lateinit var btnEdit: Button
    private lateinit var btnLogout: Button
    private var currentProfile: ProfileResponse? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_profile, findViewById(R.id.container))

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_profile

        edtName = findViewById(R.id.edtName)
        spinnerCountry = findViewById(R.id.spinnerCountry)
        spinnerCity = findViewById(R.id.spinnerCity)
        edtPassword = findViewById(R.id.edtPassword)
        btnEdit = findViewById(R.id.btnEdit)
        btnLogout = findViewById(R.id.btnLogOut)

        // Disable EditTexts initially
        setEditTextsEnabled(false)

        setupCountrySpinner()

        btnEdit.setOnClickListener {
            val isEnabled = edtName.isEnabled
            if (isEnabled) {
                // Save profile
                currentProfile?.let { profile ->
                    val fullName = edtName.text.toString()

                    val selectedCountry = spinnerCountry.selectedItem.toString()
                    val selectedCity = spinnerCity.selectedItem.toString()

                    val countryId = CountryCityData.countries[selectedCountry] ?: profile.countryId
                    val cityId = CountryCityData.cities[countryId]?.indexOf(selectedCity)?.plus(1) ?: profile.cityId

                    val updatedProfile = ProfileRequestUpdate(
                        fullName = fullName,
                        emailAddress = profile.email,
                        countryId = countryId,
                        cityId = cityId,
                        subscriptionId = profile.subscriptionId
                    )

                    val token = SessionManager.token
                    val profileServiceImpl = ProfileServiceImpl(RetrofitClient.profileService)
                    if (token != null) {
                        profileServiceImpl.updateProfile(token, profile.id,updatedProfile) { success ->
                            if (success != null) {
                                // Update current profile with new data
                                currentProfile = profile.copy(
                                    fullName = fullName,
                                    countryId = countryId,
                                    cityId = cityId
                                )
                            }
                        }
                    }
                }
            }
            setEditTextsEnabled(!isEnabled)
            btnEdit.text = if (isEnabled) "Edit" else "Save"
            btnEdit.setBackgroundColor(if (isEnabled) getColor(R.color.allow) else getColor(R.color.deny))
        }

        btnLogout.setOnClickListener {
            //Start the login activity
            SessionManager.signOut()
            startActivity(Intent(this, SignInActivity::class.java))

        }

        val token = SessionManager.token
        val profileServiceImpl = ProfileServiceImpl(RetrofitClient.profileService)

        if (token != null) {
            profileServiceImpl.getAllProfiles(token) { profiles ->
                profiles?.let {
                    val profile = it.find { profile -> profile.email == SessionManager.username }
                    profile?.let {
                        currentProfile = it
                        updateUI(it)
                    }
                }
            }
        }
    }

    private fun setEditTextsEnabled(enabled: Boolean) {
        edtName.isEnabled = enabled
        spinnerCountry.isEnabled = enabled
        spinnerCity.isEnabled = enabled
        edtPassword.isEnabled = enabled
    }

    private fun updateUI(profile: ProfileResponse) {
        edtName.setText(profile.fullName)
        val countryName = CountryCityData.countries.entries.find { it.value == profile.countryId }?.key
        val cityList = CountryCityData.cities[profile.countryId]

        // Ensure cityId is within bounds
        val cityName = if (cityList != null && profile.cityId - 1 in cityList.indices) {
            cityList[profile.cityId - 1]
        } else {
            null
        }

        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, CountryCityData.countries.keys.toList())
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = countryAdapter
        spinnerCountry.setSelection(countryAdapter.getPosition(countryName))

        countryName?.let {
            setupCitySpinner(CountryCityData.countries[it])
        }
        spinnerCity.setSelection(cityList?.indexOf(cityName) ?: 0)

        // Assuming you have a way to get the password, otherwise remove this line
        edtPassword.setText("********") // Placeholder for password
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