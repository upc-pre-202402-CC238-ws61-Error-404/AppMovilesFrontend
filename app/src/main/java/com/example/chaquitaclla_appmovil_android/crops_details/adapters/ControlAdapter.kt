// ControlAdapter.kt
package com.example.chaquitaclla_appmovil_android.crops_details.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.R
import Entities.Control

class ControlAdapter(
    private val controls: List<Control>,
    private val onEditClick: (Control) -> Unit,
    private val onDeleteClick: (Control) -> Unit
) : RecyclerView.Adapter<ControlAdapter.ControlViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControlViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_control, parent, false)
        return ControlViewHolder(view)
    }

    override fun onBindViewHolder(holder: ControlViewHolder, position: Int) {
        val control = controls[position]
        holder.bind(control)
    }

    override fun getItemCount(): Int = controls.size

    inner class ControlViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sowingConditionTextView: TextView = itemView.findViewById(R.id.sowingConditionTextView)
        private val stemConditionTextView: TextView = itemView.findViewById(R.id.stemConditionTextView)
        private val soilMoistureTextView: TextView = itemView.findViewById(R.id.soilMoistureTextView)
        private val editButton: ImageView = itemView.findViewById(R.id.editButton)
        private val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)

        fun bind(control: Control) {
            sowingConditionTextView.text = control.sowingCondition
            stemConditionTextView.text = control.stemCondition
            soilMoistureTextView.text = control.sowingSoilMoisture

            editButton.setOnClickListener { onEditClick(control) }
            deleteButton.setOnClickListener { onDeleteClick(control) }
        }
    }
}