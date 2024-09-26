package com.example.chaquitaclla_appmovil_android.iam.activitys

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.iam.RetrofitClient
import com.example.chaquitaclla_appmovil_android.iam.beans.SignUpRequest
import com.example.chaquitaclla_appmovil_android.iam.services.AuthServiceImpl

class SignUpActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnSignUp: Button = findViewById(R.id.btnSignUp)
        val authServiceImpl = AuthServiceImpl(RetrofitClient.authService)

        btnSignUp.setOnClickListener {
            val username = findViewById<EditText>(R.id.edtEmail).text.toString()
            val password = findViewById<EditText>(R.id.edtPassword).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.edtConfPassword).text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword) {
                val signUpRequest = SignUpRequest(username, password)
                authServiceImpl.signUp(signUpRequest) { signUpResponse ->
                    if (signUpResponse != null) {
                        Toast.makeText(this, signUpResponse.message, Toast.LENGTH_LONG).show()
                        startActivity(GoSignIn(this))
                    } else {
                        Toast.makeText(this, "Signup failed", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        fun GoSignIn(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }
}