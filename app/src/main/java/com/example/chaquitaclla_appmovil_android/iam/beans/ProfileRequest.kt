package com.example.chaquitaclla_appmovil_android.iam.beans

data class ProfileRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val cityId: Int,
    val subscriptionId: Int,
    val countryId: Int
)
