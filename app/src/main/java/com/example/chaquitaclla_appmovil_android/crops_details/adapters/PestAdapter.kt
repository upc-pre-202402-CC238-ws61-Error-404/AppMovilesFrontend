package com.example.chaquitaclla_appmovil_android.crops_details.adapters

import android.text.Html
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

    override fun getItemCount(): Int = pests.size

    inner class PestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.pestNameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.pestDescriptionTextView)
        private val solutionTextView: TextView = itemView.findViewById(R.id.pestSolutionTextView)

        fun bind(pest: Pest) {
            nameTextView.text = pest.name
            descriptionTextView.text = Html.fromHtml("<b>Description:</b> ${pest.description}")
            solutionTextView.text = Html.fromHtml("<b>Solution:</b> ${pest.solution}")

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