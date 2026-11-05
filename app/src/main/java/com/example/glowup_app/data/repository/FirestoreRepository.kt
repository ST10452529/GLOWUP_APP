package com.example.glowup_app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.glowup_app.data.models.DiaryEntry
import com.example.glowup_app.data.models.RoutineStep
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class FirestoreRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("glowup_data_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Key names
    private val KEY_ROUTINE_STEPS = "routine_steps"
    private val KEY_DIARY_ENTRIES = "diary_entries"

    init {
        // Seed default initial routines if empty
        if (!prefs.contains(KEY_ROUTINE_STEPS)) {
            val defaultSteps = listOf(
                RoutineStep(UUID.randomUUID().toString(), "Gentle Cleanser", "Daily Foam Wash", isMorning = true, isCompleted = false),
                RoutineStep(UUID.randomUUID().toString(), "Vitamin C Serum", "Glow Boost Serum", isMorning = true, isCompleted = false),
                RoutineStep(UUID.randomUUID().toString(), "Hydrating Moisturiser", "Daily Moisture Cream", isMorning = true, isCompleted = false),
                RoutineStep(UUID.randomUUID().toString(), "Sunscreen SPF 50", "UV Protect Shield", isMorning = true, isCompleted = false),
                RoutineStep(UUID.randomUUID().toString(), "Oil Cleanser", "Deep Cleansing Oil", isMorning = false, isCompleted = false),
                RoutineStep(UUID.randomUUID().toString(), "Night Treatment", "Retinol Night Cream", isMorning = false, isCompleted = false),
                RoutineStep(UUID.randomUUID().toString(), "Night Moisturiser", "Rich Repair Balm", isMorning = false, isCompleted = false)
            )
            saveRoutineSteps(defaultSteps)
        }

        if (!prefs.contains(KEY_DIARY_ENTRIES)) {
            val defaultEntries = listOf(
                DiaryEntry(
                    id = UUID.randomUUID().toString(),
                    date = getFormattedDate(),
                    tag = "✨ Glowing",
                    content = "Welcome to GlowUp! Started my new skincare journey today."
                )
            )
            saveDiaryEntries(defaultEntries)
        }
    }

    // --- ROUTINE MANAGEMENT ---

    fun getRoutineSteps(): List<RoutineStep> {
        val json = prefs.getString(KEY_ROUTINE_STEPS, null) ?: return emptyList()
        val type = object : TypeToken<List<RoutineStep>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun saveRoutineSteps(steps: List<RoutineStep>) {
        val json = gson.toJson(steps)
        prefs.edit().putString(KEY_ROUTINE_STEPS, json).apply()
    }

    fun addRoutineStep(step: RoutineStep) {
        val steps = getRoutineSteps().toMutableList()
        steps.add(step)
        saveRoutineSteps(steps)
    }

    fun toggleStepCompletion(stepId: String, isCompleted: Boolean) {
        val steps = getRoutineSteps().map {
            if (it.id == stepId) it.copy(isCompleted = isCompleted) else it
        }
        saveRoutineSteps(steps)
    }

    fun deleteRoutineStep(stepId: String) {
        val steps = getRoutineSteps().filterNot { it.id == stepId }
        saveRoutineSteps(steps)
    }

    fun resetDailyProgress() {
        val steps = getRoutineSteps().map { it.copy(isCompleted = false) }
        saveRoutineSteps(steps)
    }

    // --- DIARY MANAGEMENT ---

    fun getDiaryEntries(): List<DiaryEntry> {
        val json = prefs.getString(KEY_DIARY_ENTRIES, null) ?: return emptyList()
        val type = object : TypeToken<List<DiaryEntry>>() {}.type
        val list: List<DiaryEntry> = gson.fromJson(json, type) ?: emptyList()
        return list.sortedByDescending { it.timestamp }
    }

    fun saveDiaryEntries(entries: List<DiaryEntry>) {
        val json = gson.toJson(entries)
        prefs.edit().putString(KEY_DIARY_ENTRIES, json).apply()
    }

    fun addDiaryEntry(tag: String, content: String) {
        val newEntry = DiaryEntry(
            id = UUID.randomUUID().toString(),
            date = getFormattedDate(),
            tag = tag,
            content = content,
            timestamp = System.currentTimeMillis()
        )
        val entries = getDiaryEntries().toMutableList()
        entries.add(0, newEntry)
        saveDiaryEntries(entries)
    }

    fun deleteDiaryEntry(entryId: String) {
        val entries = getDiaryEntries().filterNot { it.id == entryId }
        saveDiaryEntries(entries)
    }

    private fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}
