package com.example.chaquitaclla_appmovil_android.crops_details.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Cares
import java.text.SimpleDateFormat
import java.util.Locale

class CropCareAdapter(private val caresList: List<Cares>) : RecyclerView.Adapter<CropCareAdapter.CropCareViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropCareViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_care, parent, false)
        return CropCareViewHolder(view)
    }

    override fun onBindViewHolder(holder: CropCareViewHolder, position: Int) {
        val care = caresList[position]
        holder.bind(care)
    }

    override fun getItemCount(): Int {
        return caresList.size
    }

    class CropCareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val suggestionTextView: TextView = itemView.findViewById(R.id.suggesetionTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        fun bind(care: Cares) {
            suggestionTextView.text = care.suggestion
            dateTextView.text = dateFormat.format(care.date)
        }
    }
}