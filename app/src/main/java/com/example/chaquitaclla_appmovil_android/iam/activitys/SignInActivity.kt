package com.example.chaquitaclla_appmovil_android.iam.activitys

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.SessionManager
import com.example.chaquitaclla_appmovil_android.iam.RetrofitClient
import com.example.chaquitaclla_appmovil_android.iam.beans.SignInRequest
import com.example.chaquitaclla_appmovil_android.iam.services.AuthServiceImpl
import com.example.chaquitaclla_appmovil_android.iam.services.ProfileServiceImpl

class SignInActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rlLogIn)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        val authServiceImpl = AuthServiceImpl(RetrofitClient.authService)
        val profileServiceImpl = ProfileServiceImpl(RetrofitClient.profileService)

        btnLogin.setOnClickListener {
            val username = findViewById<EditText>(R.id.edtUser).text.toString()
            val password = findViewById<EditText>(R.id.edtPassword).text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                val signInRequest = SignInRequest(username, password)
                val url = "https://localhost:7071/api/v1/authentication/sign-in"

                authServiceImpl.signIn(signInRequest) { signInResponse ->
                    if (signInResponse != null) {
                        SessionManager.signInResponse = signInResponse
                        Toast.makeText(this, "Welcome ${signInResponse.username}", Toast.LENGTH_LONG).show()
                        profileServiceImpl.getAllProfiles(SessionManager.token!!) { profiles ->
                            val profile = profiles?.find { it.email == username }
                            if (profile != null) {
                                SessionManager.profileId = profile.id
                                startActivity(GoProfile(this))
                            } else {
                                startActivity(GoPlans(this))
                            }
                        }
                    } else {
                        Log.e("SignInActivity", "Login failed: response is null")
                        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Log.w("SignInActivity", "Username or password is empty")
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }

        btnSignUp.setOnClickListener {
            startActivity(GoSignup(this))
        }
    }

    companion object {
        fun GoSignup(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
        fun GoPlans(context: Context): Intent {
            return Intent(context, PlansActivity::class.java)
        }
        fun GoProfile(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }
}