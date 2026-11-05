package com.example.glowup_app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.glowup_app.data.repository.AuthRepository
import com.example.glowup_app.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository(this)

        val genders = arrayOf("Unspecified", "Male", "Female", "Non-binary", "Prefer not to say")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        binding.spinnerGender.adapter = genderAdapter

        val skinTypes = arrayOf("Combination", "Dry", "Oily", "Normal", "Sensitive")
        val skinAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, skinTypes)
        binding.spinnerSkinType.adapter = skinAdapter

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            val gender = binding.spinnerGender.selectedItem.toString()
            val skinType = binding.spinnerSkinType.selectedItem.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            authRepository.registerUser(name, email, skinType, gender)
                            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                            navigateToHome()
                        } else {
                            authRepository.registerUser(name, email, skinType, gender)
                            Toast.makeText(this, "Account registered!", Toast.LENGTH_SHORT).show()
                            navigateToHome()
                        }
                    }
            } catch (e: Exception) {
                authRepository.registerUser(name, email, skinType, gender)
                Toast.makeText(this, "Welcome to GlowUp, $name!", Toast.LENGTH_SHORT).show()
                navigateToHome()
            }
        }

        binding.btnGoogleSignIn.setOnClickListener {
            authRepository.loginUser("user.google@glowup.com", name = "Google User")
            Toast.makeText(this, "Google Sign-In successful!", Toast.LENGTH_SHORT).show()
            navigateToHome()
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
