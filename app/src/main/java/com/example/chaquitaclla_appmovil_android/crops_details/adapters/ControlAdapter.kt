// app/src/main/java/com/example/chaquitaclla_appmovil_android/crops_details/adapters/ControlAdapter.kt
package com.example.chaquitaclla_appmovil_android.crops_details.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquitaclla_appmovil_android.R
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Controls

class ControlAdapter(private val controls: List<Controls>, private val onEditClick: (Controls) -> Unit, private val onDeleteClick: (Controls) -> Unit) : RecyclerView.Adapter<ControlAdapter.ControlViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControlViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_control, parent, false)
        return ControlViewHolder(view)
    }

    override fun onBindViewHolder(holder: ControlViewHolder, position: Int) {
        val control = controls[position]
        holder.bind(control, onEditClick, onDeleteClick)
    }

    override fun getItemCount(): Int {
        return controls.size
    }

    class ControlViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sowingConditionTextView: TextView = itemView.findViewById(R.id.sowingConditionTextView)
        private val stemConditionTextView: TextView = itemView.findViewById(R.id.stemConditionTextView)
        private val soilMoistureTextView: TextView = itemView.findViewById(R.id.soilMoistureTextView)
        private val editButton: Button = itemView.findViewById(R.id.editButton)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(control: Controls, onEditClick: (Controls) -> Unit, onDeleteClick: (Controls) -> Unit) {
            sowingConditionTextView.text = control.condition.toString()
            stemConditionTextView.text = control.stemCondition.toString()
            soilMoistureTextView.text = control.soilMoisture.toString()

            editButton.setOnClickListener { onEditClick(control) }
            deleteButton.setOnClickListener { onDeleteClick(control) }
        }
    }
}