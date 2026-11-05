package com.example.glowup_app

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.glowup_app.data.models.RoutineStep
import com.example.glowup_app.data.repository.FirestoreRepository
import com.example.glowup_app.databinding.ActivityRoutineBinding
import com.example.glowup_app.ui.adapters.RoutineAdapter
import java.util.UUID

class RoutineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoutineBinding
    private lateinit var firestoreRepository: FirestoreRepository

    private lateinit var morningAdapter: RoutineAdapter
    private lateinit var eveningAdapter: RoutineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreRepository = FirestoreRepository(this)

        setupRecyclerViews()

        binding.btnAddMorningStep.setOnClickListener {
            showAddStepDialog(isMorning = true)
        }

        binding.btnAddEveningStep.setOnClickListener {
            showAddStepDialog(isMorning = false)
        }

        binding.btnResetDaily.setOnClickListener {
            firestoreRepository.resetDailyProgress()
            loadRoutines()
            Toast.makeText(this, "Daily routine progress reset!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadRoutines()
    }

    private fun setupRecyclerViews() {
        morningAdapter = RoutineAdapter(
            steps = emptyList(),
            onCheckedChange = { step, isChecked ->
                firestoreRepository.toggleStepCompletion(step.id, isChecked)
            },
            onDeleteClick = { step ->
                firestoreRepository.deleteRoutineStep(step.id)
                loadRoutines()
                Toast.makeText(this, "Step deleted", Toast.LENGTH_SHORT).show()
            }
        )

        eveningAdapter = RoutineAdapter(
            steps = emptyList(),
            onCheckedChange = { step, isChecked ->
                firestoreRepository.toggleStepCompletion(step.id, isChecked)
            },
            onDeleteClick = { step ->
                firestoreRepository.deleteRoutineStep(step.id)
                loadRoutines()
                Toast.makeText(this, "Step deleted", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvMorningRoutine.layoutManager = LinearLayoutManager(this)
        binding.rvMorningRoutine.adapter = morningAdapter

        binding.rvEveningRoutine.layoutManager = LinearLayoutManager(this)
        binding.rvEveningRoutine.adapter = eveningAdapter
    }

    private fun loadRoutines() {
        val allSteps = firestoreRepository.getRoutineSteps()
        val morningSteps = allSteps.filter { it.isMorning }
        val eveningSteps = allSteps.filter { !it.isMorning }

        morningAdapter.updateData(morningSteps)
        eveningAdapter.updateData(eveningSteps)
    }

    private fun showAddStepDialog(isMorning: Boolean) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_step, null)
        val etStepName = dialogView.findViewById<EditText>(R.id.etStepName)
        val etProductName = dialogView.findViewById<EditText>(R.id.etProductName)

        val title = if (isMorning) "Add Morning Step ☀️" else "Add Evening Step 🌙"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val stepName = etStepName.text.toString().trim()
                val productName = etProductName.text.toString().trim()

                if (stepName.isNotEmpty()) {
                    val newStep = RoutineStep(
                        id = UUID.randomUUID().toString(),
                        title = stepName,
                        productName = if (productName.isNotEmpty()) productName else "General Product",
                        isMorning = isMorning,
                        isCompleted = false
                    )
                    firestoreRepository.addRoutineStep(newStep)
                    loadRoutines()
                    Toast.makeText(this, "Step added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Step name cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
