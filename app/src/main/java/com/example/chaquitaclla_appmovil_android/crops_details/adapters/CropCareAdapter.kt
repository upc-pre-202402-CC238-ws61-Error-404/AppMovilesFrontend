// app/src/main/java/com/example/chaquitaclla_appmovil_android/CropCareAdapter.kt
package com.example.chaquitaclla_appmovil_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Cares

class CropCareAdapter(private val caresList: List<Cares>) : RecyclerView.Adapter<CropCareAdapter.CropCareViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropCareViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_care, parent, false)
        return CropCareViewHolder(view)
    }

    override fun onBindViewHolder(holder: CropCareViewHolder, position: Int) {
        val care = caresList[position]
        holder.bind(care)
    }

    override fun getItemCount(): Int = caresList.size

    class CropCareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(care: Cares) {
            nameTextView.text = care.description
            descriptionTextView.text = care.description
        }
    }
}