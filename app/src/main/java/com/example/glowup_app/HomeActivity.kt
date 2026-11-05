package com.example.glowup_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.glowup_app.data.repository.AuthRepository
import com.example.glowup_app.data.repository.FirestoreRepository
import com.example.glowup_app.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var firestoreRepository: FirestoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository(this)
        firestoreRepository = FirestoreRepository(this)

        binding.btnRoutine.setOnClickListener {
            startActivity(Intent(this, RoutineActivity::class.java))
        }

        binding.btnProducts.setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }

        binding.btnDiary.setOnClickListener {
            startActivity(Intent(this, DiaryActivity::class.java))
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateDashboard()
    }

    private fun updateDashboard() {
        val user = authRepository.getCurrentUser()
        binding.tvGreeting.text = "Welcome, ${user.name} ✨"

        val steps = firestoreRepository.getRoutineSteps()
        val morningSteps = steps.filter { it.isMorning }
        val eveningSteps = steps.filter { !it.isMorning }

        val completedMorning = morningSteps.count { it.isCompleted }
        val completedEvening = eveningSteps.count { it.isCompleted }
        val totalSteps = steps.size
        val totalCompleted = steps.count { it.isCompleted }

        val progressPercent = if (totalSteps > 0) (totalCompleted * 100) / totalSteps else 0

        binding.progressToday.progress = progressPercent
        binding.tvProgressText.text = "$progressPercent%"
        binding.tvRoutineSummary.text = "☀️ Morning: $completedMorning/${morningSteps.size} completed\n🌙 Evening: $completedEvening/${eveningSteps.size} completed"
    }
}
