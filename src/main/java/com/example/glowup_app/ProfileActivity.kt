package com.example.glowup_app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.glowup_app.data.network.GlowNotificationManager
import com.example.glowup_app.data.repository.AuthRepository
import com.example.glowup_app.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository(this)

        updateProfileUI()

        // Setup Dark Mode Switch
        binding.switchDarkMode.isChecked = authRepository.isDarkMode
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            authRepository.setDarkMode(isChecked)
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        binding.btnChangeLanguage.setOnClickListener {
            showLanguageDialog()
        }

        binding.btnSendNotification.setOnClickListener {
            GlowNotificationManager.sendSkincareReminder(
                this,
                title = "✨ GlowUp Skincare Reminder",
                message = "Time for your daily skincare routine! Keep glowing ✨"
            )
            Toast.makeText(this, "Notification sent!", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            authRepository.logout()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun updateProfileUI() {
        val user = authRepository.getCurrentUser()
        binding.tvProfileName.text = user.name
        binding.tvProfileEmail.text = user.email
        binding.tvSkinType.text = "Skin: ${user.skinType}"
        binding.tvGender.text = "Gender: ${user.gender}"
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English 🇬🇧", "isiZulu 🇿🇦", "Sesotho 🇿🇦")
        val localeTags = arrayOf("en", "zu", "st")

        AlertDialog.Builder(this)
            .setTitle("Select App Language / Puo / Ulimi 🇿🇦")
            .setItems(languages) { dialog, which ->
                val selectedTag = localeTags[which]
                val appLocale = LocaleListCompat.forLanguageTags(selectedTag)
                AppCompatDelegate.setApplicationLocales(appLocale)
                Toast.makeText(this, "Language updated to ${languages[which]}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditProfileDialog() {
        val user = authRepository.getCurrentUser()
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null)
        val etEditName = dialogView.findViewById<EditText>(R.id.etEditName)
        val spinnerGender = dialogView.findViewById<Spinner>(R.id.spinnerGender)
        val spinnerSkinType = dialogView.findViewById<Spinner>(R.id.spinnerSkinType)

        etEditName.setText(user.name)

        val genders = arrayOf("Unspecified", "Male", "Female", "Non-binary", "Prefer not to say")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        spinnerGender.adapter = genderAdapter
        val genderIndex = genders.indexOf(user.gender).let { if (it >= 0) it else 0 }
        spinnerGender.setSelection(genderIndex)

        val skinTypes = arrayOf("Combination", "Dry", "Oily", "Normal", "Sensitive")
        val skinAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, skinTypes)
        spinnerSkinType.adapter = skinAdapter
        val skinIndex = skinTypes.indexOf(user.skinType).let { if (it >= 0) it else 0 }
        spinnerSkinType.setSelection(skinIndex)

        AlertDialog.Builder(this)
            .setTitle("Edit Profile & Preferences 👤")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val newName = etEditName.text.toString().trim()
                val selectedGender = spinnerGender.selectedItem.toString()
                val selectedSkinType = spinnerSkinType.selectedItem.toString()

                if (newName.isNotEmpty()) {
                    authRepository.updateUserProfile(
                        name = newName,
                        gender = selectedGender,
                        skinType = selectedSkinType
                    )
                    updateProfileUI()
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
