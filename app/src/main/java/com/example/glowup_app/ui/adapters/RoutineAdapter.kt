package com.example.glowup_app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glowup_app.data.models.RoutineStep
import com.example.glowup_app.databinding.ItemRoutineStepBinding

class RoutineAdapter(
    private var steps: List<RoutineStep>,
    private val onCheckedChange: (RoutineStep, Boolean) -> Unit,
    private val onDeleteClick: (RoutineStep) -> Unit
) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(val binding: ItemRoutineStepBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val binding = ItemRoutineStepBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoutineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val step = steps[position]
        with(holder.binding) {
            tvStepTitle.text = step.title
            tvStepProduct.text = step.productName

            cbStep.setOnCheckedChangeListener(null)
            cbStep.isChecked = step.isCompleted

            cbStep.setOnCheckedChangeListener { _, isChecked ->
                step.isCompleted = isChecked
                onCheckedChange(step, isChecked)
            }

            btnDeleteStep.setOnClickListener {
                onDeleteClick(step)
            }
        }
    }

    override fun getItemCount(): Int = steps.size

    fun updateData(newSteps: List<RoutineStep>) {
        steps = newSteps
        notifyDataSetChanged()
    }
}
