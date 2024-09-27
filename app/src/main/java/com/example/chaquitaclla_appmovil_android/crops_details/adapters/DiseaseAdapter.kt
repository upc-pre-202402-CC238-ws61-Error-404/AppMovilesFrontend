package com.example.chaquitaclla_appmovil_android.crops_details.adapters

import android.text.Html.fromHtml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Disease

class DiseaseAdapter(private val diseases: List<Disease>) : RecyclerView.Adapter<DiseaseAdapter.ViewHolder>() {

    private var filteredDiseases: List<Disease> = diseases
    private var expandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_disease, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val disease = filteredDiseases[position]
        holder.bind(disease, position)
    }

    override fun getItemCount(): Int {
        return filteredDiseases.size
    }

    fun filter(query: String) {
        filteredDiseases = if (query.isEmpty()) {
            diseases
        } else {
            diseases.filter { it.name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val solutionTextView: TextView = itemView.findViewById(R.id.solutionTextView)

        fun bind(disease: Disease, position: Int) {
            nameTextView.text = disease.name
            descriptionTextView.text = fromHtml("<b>Description</b>: ${disease.description}")
            solutionTextView.text = fromHtml("<b>Solution</b>: ${disease.solution}")

            val isExpanded = position == expandedPosition
            descriptionTextView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            solutionTextView.visibility = if (isExpanded) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                expandedPosition = if (isExpanded) -1 else position
                notifyDataSetChanged()
            }
        }
    }
}