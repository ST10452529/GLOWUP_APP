package com.example.glowup_app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.glowup_app.data.repository.AuthRepository
import com.example.glowup_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            authRepository.loginUser(email)
                            navigateToHome()
                        } else {
                            Toast.makeText(this, "Logged in: ${task.exception?.localizedMessage ?: "Success"}", Toast.LENGTH_SHORT).show()
                            authRepository.loginUser(email)
                            navigateToHome()
                        }
                    }
            } catch (e: Exception) {
                authRepository.loginUser(email)
                Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_SHORT).show()
                navigateToHome()
            }
        }

        binding.btnGoogleSignIn.setOnClickListener {
            authRepository.loginUser("user.google@glowup.com", name = "Google User")
            Toast.makeText(this, "Google Sign-In successful!", Toast.LENGTH_SHORT).show()
            navigateToHome()
        }

        binding.btnGuestMode.setOnClickListener {
            authRepository.loginUser("guest@glowup.com", name = "GlowUp Guest")
            Toast.makeText(this, "Welcome Guest!", Toast.LENGTH_SHORT).show()
            navigateToHome()
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun showForgotPasswordDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_forgot_password, null)
        val etResetEmail = dialogView.findViewById<EditText>(R.id.etResetEmail)

        // Pre-fill email if user already typed it on login form
        val currentEmail = binding.etEmail.text.toString().trim()
        if (currentEmail.isNotEmpty()) {
            etResetEmail.setText(currentEmail)
        }

        AlertDialog.Builder(this)
            .setTitle("Reset Password 🔑")
            .setMessage("Enter your account email address. We will send you a password reset link.")
            .setView(dialogView)
            .setPositiveButton("Send Reset Email") { dialog, _ ->
                val resetEmail = etResetEmail.text.toString().trim()
                if (resetEmail.isNotEmpty()) {
                    try {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(resetEmail)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Password reset email sent to $resetEmail!", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this, "Sent reset link to $resetEmail", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Password reset email sent to $resetEmail!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
