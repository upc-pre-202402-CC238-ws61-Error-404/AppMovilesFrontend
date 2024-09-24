package com.example.chaquitaclla_appmovil_android.forum.beans

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormat {
    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

        fun format(date: Date): String {
            return dateFormat.format(date)
        }
    }
}