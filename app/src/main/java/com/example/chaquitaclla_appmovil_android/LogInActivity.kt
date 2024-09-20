package com.example.chaquitaclla_appmovil_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaquitaclla_appmovil_android.`interface`.AuthService
import com.example.chaquitaclla_appmovil_android.io.RetrofitClient
import com.example.chaquitaclla_appmovil_android.model.SignUpRequest
import com.example.chaquitaclla_appmovil_android.model.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rlLogIn)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authService = RetrofitClient.instance.create(AuthService::class.java)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        btnLogin.setOnClickListener {
            val username = findViewById<EditText>(R.id.edtUser).text.toString()
            val password = findViewById<EditText>(R.id.edtPassword).text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                login(username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }
        btnSignUp.setOnClickListener {
            startActivity(GoSignup(this))
        }
    }

    private fun login(username: String, password: String) {
        val request = SignUpRequest(username, password)
        authService.signIn(request).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    Toast.makeText(this@LogInActivity, "Login Successful! Token: $token", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@LogInActivity, "Login Failed!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(this@LogInActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        fun GoSignup(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}