package com.example.chaquitaclla_appmovil_android.crops_details.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Disease

class DiseaseAdapter(private val diseases: List<Disease>) : RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_disease, parent, false)
        return DiseaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        val disease = diseases[position]
        holder.bind(disease)
    }

    override fun getItemCount(): Int = diseases.size

    inner class DiseaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val solutionTextView: TextView = itemView.findViewById(R.id.solutionTextView)

        fun bind(disease: Disease) {
            nameTextView.text = disease.name
            descriptionTextView.text = disease.description
            solutionTextView.text = disease.solution

            nameTextView.setOnClickListener {
                if (descriptionTextView.visibility == View.GONE) {
                    descriptionTextView.visibility = View.VISIBLE
                    solutionTextView.visibility = View.VISIBLE
                } else {
                    descriptionTextView.visibility = View.GONE
                    solutionTextView.visibility = View.GONE
                }
            }
        }
    }
}