package com.example.glowup_app.data.models

data class DiaryEntry(
    val id: String = "",
    val date: String = "",
    val tag: String = "✨ Glowing",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
