package com.example.chaquitaclla_appmovil_android.iam.beans

data class ProfileRequestUpdate (
    val fullName: String,
    val emailAddress: String,
    val countryId: Int,
    val cityId: Int,
    val subscriptionId: Int
)
