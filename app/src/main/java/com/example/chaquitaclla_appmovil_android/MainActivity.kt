package com.example.chaquitaclla_appmovil_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaquitaclla_appmovil_android.forum.activitys.ForumManagementActivity
import com.example.chaquitaclla_appmovil_android.iam.activitys.SignInActivity
import com.example.chaquitaclla_appmovil_android.iam.activitys.SignUpActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajustar el padding para los system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Botón para ir a LogInActivity
        val button: Button = findViewById(R.id.buttonLogin)
        button.setOnClickListener {
            startActivity(goLogin(this))
        }

        // Botón para ir a SignUpActivity
        val buttonSignUp: Button = findViewById(R.id.buttonRegister)
        buttonSignUp.setOnClickListener {
            startActivity(GoSignup(this))
        }

        val buttonForum: Button = findViewById(R.id.btnForum)

        buttonForum.setOnClickListener {
            startActivity(newIntentForum(this))
        }
    }

    companion object {
        fun goLogin(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }

        fun GoSignup(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
        fun newIntentForum(context: Context): Intent{
            return Intent(context, ForumManagementActivity::class.java)
        }
    }
}