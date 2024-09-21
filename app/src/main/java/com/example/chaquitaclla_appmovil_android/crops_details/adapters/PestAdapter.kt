package com.example.chaquitaclla_appmovil_android.crops_details.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Pest

class PestAdapter(private val pests: List<Pest>) : RecyclerView.Adapter<PestAdapter.PestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pest, parent, false)
        return PestViewHolder(view)
    }

    override fun onBindViewHolder(holder: PestViewHolder, position: Int) {
        val pest = pests[position]
        holder.bind(pest)
    }

    override fun getItemCount(): Int {
        return pests.size
    }

    class PestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pestNameTextView: TextView = itemView.findViewById(R.id.pestTitleTextView)
        private val pestDescriptionTextView: TextView = itemView.findViewById(R.id.pestDescriptionTextView)

        fun bind(pest: Pest) {
            pestNameTextView.text = pest.name
            pestDescriptionTextView.text = pest.description
        }
    }
}