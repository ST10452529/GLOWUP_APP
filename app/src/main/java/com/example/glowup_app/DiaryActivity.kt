package com.example.glowup_app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.glowup_app.data.repository.FirestoreRepository
import com.example.glowup_app.databinding.ActivityDiaryBinding
import com.example.glowup_app.ui.adapters.DiaryAdapter

class DiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryBinding
    private lateinit var firestoreRepository: FirestoreRepository
    private lateinit var diaryAdapter: DiaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreRepository = FirestoreRepository(this)

        setupRecyclerView()

        binding.btnSaveDiary.setOnClickListener {
            val content = binding.etDiary.text.toString().trim()
            if (content.isEmpty()) {
                Toast.makeText(this, "Please write something before saving", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedTag = when (binding.chipGroupTag.checkedChipId) {
                R.id.chipGlowing -> "✨ Glowing"
                R.id.chipHydrated -> "💧 Hydrated"
                R.id.chipDry -> "🌵 Dry / Irritated"
                R.id.chipBreakout -> "🔴 Breakout"
                else -> "✨ Glowing"
            }

            firestoreRepository.addDiaryEntry(selectedTag, content)
            binding.etDiary.text.clear()
            loadEntries()
            Toast.makeText(this, "Diary entry saved!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadEntries()
    }

    private fun setupRecyclerView() {
        diaryAdapter = DiaryAdapter(
            entries = emptyList(),
            onDeleteClick = { entry ->
                firestoreRepository.deleteDiaryEntry(entry.id)
                loadEntries()
                Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvDiaryEntries.layoutManager = LinearLayoutManager(this)
        binding.rvDiaryEntries.adapter = diaryAdapter
    }

    private fun loadEntries() {
        val entries = firestoreRepository.getDiaryEntries()
        diaryAdapter.updateData(entries)
    }
}
