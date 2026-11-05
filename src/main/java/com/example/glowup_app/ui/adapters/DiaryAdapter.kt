package com.example.glowup_app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glowup_app.data.models.DiaryEntry
import com.example.glowup_app.databinding.ItemDiaryEntryBinding

class DiaryAdapter(
    private var entries: List<DiaryEntry>,
    private val onDeleteClick: (DiaryEntry) -> Unit
) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    class DiaryViewHolder(val binding: ItemDiaryEntryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val binding = ItemDiaryEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val entry = entries[position]
        with(holder.binding) {
            tvDiaryDate.text = entry.date
            tvDiaryTag.text = entry.tag
            tvDiaryContent.text = entry.content

            btnDeleteEntry.setOnClickListener {
                onDeleteClick(entry)
            }
        }
    }

    override fun getItemCount(): Int = entries.size

    fun updateData(newEntries: List<DiaryEntry>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}
