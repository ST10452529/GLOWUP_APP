package com.example.glowup_app.data.models

data class RoutineStep(
    val id: String = "",
    val title: String = "",
    val productName: String = "",
    val isMorning: Boolean = true,
    var isCompleted: Boolean = false
)
