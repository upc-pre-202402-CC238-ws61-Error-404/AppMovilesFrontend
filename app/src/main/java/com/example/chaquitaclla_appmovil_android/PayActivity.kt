package com.example.chaquitaclla_appmovil_android

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.chaquitaclla_appmovil_android.MainActivity.Companion.goLogin
import java.util.regex.Pattern

class PayActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        val btnPay: Button = findViewById(R.id.btnPay)
        btnPay.setOnClickListener {
            startActivity(goProfile(this))
        }

        val edtSecuCode: EditText = findViewById(R.id.edtSecuCode)
        edtSecuCode.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))

        val edtDueDate: EditText = findViewById(R.id.edtDueDate)
        edtDueDate.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val ddmmyyyy = "DDMM"
            private val cal = Calendar.getInstance()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    val userInput = s.toString().replace(Regex("[^\\d]"), "")
                    if (userInput.length <= 4) {
                        val sb = StringBuilder(userInput)
                        if (userInput.length > 2) {
                            sb.insert(2, "/")
                        }
                        current = sb.toString()
                        edtDueDate.setText(current)
                        edtDueDate.setSelection(current.length)
                    } else {
                        edtDueDate.setText(current)
                        edtDueDate.setSelection(current.length)
                    }
                }
            }
        })

        val edtCardNumber: EditText = findViewById(R.id.edtCardNumber)
        edtCardNumber.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(19))
        edtCardNumber.addTextChangedListener(object : TextWatcher {
            private var isFormatting: Boolean = false
            private val space = ' '

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                isFormatting = true
                val formatted = StringBuilder()
                val digits = s.toString().replace(" ", "")
                for (i in digits.indices) {
                    if (i > 0 && i % 4 == 0) {
                        formatted.append(space)
                    }
                    formatted.append(digits[i])
                }
                edtCardNumber.setText(formatted.toString())
                edtCardNumber.setSelection(formatted.length)
                isFormatting = false
            }
        })

        val edtEmail: EditText = findViewById(R.id.edtEmail)
        edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val emailPattern = Pattern.compile(
                    "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                )
                if (!emailPattern.matcher(s.toString()).matches()) {
                    edtEmail.error = "Invalid email format"
                }
            }
        })
    }
    companion object {
        fun goProfile(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }
}