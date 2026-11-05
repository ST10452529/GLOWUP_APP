package com.example.glowup_app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.glowup_app.data.models.User
import com.google.firebase.auth.FirebaseAuth

class AuthRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("glowup_auth_prefs", Context.MODE_PRIVATE)

    private val firebaseAuth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    val isUserLoggedIn: Boolean
        get() {
            val fbUser = try { firebaseAuth?.currentUser } catch (e: Exception) { null }
            if (fbUser != null) return true
            return prefs.getBoolean("is_logged_in", false)
        }

    val currentUserId: String
        get() {
            val fbUid = try { firebaseAuth?.currentUser?.uid } catch (e: Exception) { null }
            if (!fbUid.isNullOrEmpty()) return fbUid
            return prefs.getString("user_uid", "guest_user_123") ?: "guest_user_123"
        }

    val isDarkMode: Boolean
        get() = prefs.getBoolean("is_dark_mode", false)

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("is_dark_mode", enabled).apply()
    }

    fun getCurrentUser(): User {
        val fbUser = try { firebaseAuth?.currentUser } catch (e: Exception) { null }
        val name = prefs.getString("user_name", "GlowUp User") ?: "GlowUp User"
        val email = fbUser?.email ?: (prefs.getString("user_email", "user@glowup.com") ?: "user@glowup.com")
        val skinType = prefs.getString("user_skin_type", "Combination") ?: "Combination"
        val gender = prefs.getString("user_gender", "Unspecified") ?: "Unspecified"

        return User(
            uid = currentUserId,
            name = name,
            email = email,
            skinType = skinType,
            gender = gender
        )
    }

    fun loginUser(email: String, name: String = "GlowUp User", skinType: String = "Combination", gender: String = "Unspecified") {
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("user_email", email)
            .putString("user_name", name)
            .putString("user_skin_type", skinType)
            .putString("user_gender", gender)
            .apply()
    }

    fun registerUser(name: String, email: String, skinType: String, gender: String = "Unspecified") {
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("user_name", name)
            .putString("user_email", email)
            .putString("user_skin_type", skinType)
            .putString("user_gender", gender)
            .apply()
    }

    fun updateUserProfile(name: String, gender: String, skinType: String) {
        prefs.edit()
            .putString("user_name", name)
            .putString("user_gender", gender)
            .putString("user_skin_type", skinType)
            .apply()
    }

    fun logout() {
        try {
            firebaseAuth?.signOut()
        } catch (e: Exception) {
            // ignore
        }
        prefs.edit()
            .putBoolean("is_logged_in", false)
            .apply()
    }

    fun updateSkinType(skinType: String) {
        prefs.edit().putString("user_skin_type", skinType).apply()
    }
}
