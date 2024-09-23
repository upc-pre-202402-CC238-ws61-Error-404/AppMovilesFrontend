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
import androidx.lifecycle.lifecycleScope
import com.example.chaquitaclla_appmovil_android.utils.`interface`.AuthService
import com.example.chaquitaclla_appmovil_android.utils.RetrofitClient
import com.example.chaquitaclla_appmovil_android.utils.model.UserRequest
import kotlinx.coroutines.launch
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

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        btnLogin.setOnClickListener {
            val username = findViewById<EditText>(R.id.edtUser).text.toString()
            val password = findViewById<EditText>(R.id.edtPassword).text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    login(username, password)
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }
        btnSignUp.setOnClickListener {
            startActivity(GoSignup(this))
        }
    }

    private suspend fun login(username: String, password: String) {
        val request = UserRequest(username, password = password)
        val response = RetrofitClient.authService.signIn(request)
        if (response.isSuccessful && response.body() != null) {
            val userResponse = response.body()!!

            val token = userResponse.token
            Toast.makeText(this, "Login Successful! Token: $token", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Login Failed!", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        fun GoSignup(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}