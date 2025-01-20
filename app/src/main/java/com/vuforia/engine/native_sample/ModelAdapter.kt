package com.vuforia.engine.native_sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ModelAdapter(
    private val models: List<ModelItem>,
    private val onClick: (ModelItem) -> Unit
) : RecyclerView.Adapter<ModelAdapter.ModelViewHolder>() {

    inner class ModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val modelName: TextView = view.findViewById(R.id.modelName)

        fun bind(model: ModelItem) {
            modelName.text = model.name
            itemView.setOnClickListener { onClick(model) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.model_item, parent, false)
        return ModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.bind(models[position])
    }

    override fun getItemCount() = models.size
}
