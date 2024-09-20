package com.example.chaquitaclla_appmovil_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chaquitaclla_appmovil_android.MainActivity.Companion.GoSignup
import com.example.chaquitaclla_appmovil_android.`interface`.AuthService
import com.example.chaquitaclla_appmovil_android.io.RetrofitClient
import com.example.chaquitaclla_appmovil_android.model.SignUpRequest
import com.example.chaquitaclla_appmovil_android.model.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        authService = RetrofitClient.instance.create(AuthService::class.java)

        val btnSignUp: Button = findViewById(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            val username = findViewById<EditText>(R.id.edtEmail).text.toString()
            val password = findViewById<EditText>(R.id.edtPassword).text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                signUp(username, password)
                startActivity(GoPlans(this))
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signUp(username: String, password: String) {
        val request = SignUpRequest(username, password)
        authService.signUp(request).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        Toast.makeText(this@SignUpActivity, "SignUp Successful! Token: $token", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@SignUpActivity, "SignUp Successful but token is null", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "SignUp Failed! Response: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    companion object {
        fun GoPlans(context: Context): Intent {
            return Intent(context, PlansActivity::class.java)
        }
    }
}