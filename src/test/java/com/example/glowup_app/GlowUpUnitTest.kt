package com.example.glowup_app

import com.example.glowup_app.data.models.DiaryEntry
import com.example.glowup_app.data.models.Product
import com.example.glowup_app.data.models.RoutineStep
import com.example.glowup_app.data.models.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GlowUpUnitTest {

    @Test
    fun testUserModelDefaults() {
        val user = User(uid = "123", name = "Test User", email = "test@example.com", skinType = "Dry", gender = "Male")
        assertEquals("123", user.uid)
        assertEquals("Test User", user.name)
        assertEquals("test@example.com", user.email)
        assertEquals("Dry", user.skinType)
        assertEquals("Male", user.gender)
    }

    @Test
    fun testRoutineStepToggle() {
        val step = RoutineStep(id = "step1", title = "Cleanser", productName = "Foam Wash", isMorning = true, isCompleted = false)
        assertFalse(step.isCompleted)
        step.isCompleted = true
        assertTrue(step.isCompleted)
    }

    @Test
    fun testDiaryEntryCreation() {
        val entry = DiaryEntry(id = "d1", date = "May 20, 2026", tag = "✨ Glowing", content = "Skin feels great today!")
        assertEquals("d1", entry.id)
        assertEquals("✨ Glowing", entry.tag)
        assertEquals("Skin feels great today!", entry.content)
    }

    @Test
    fun testProductMapping() {
        val product = Product(id = "p1", name = "CeraVe Cleanser", brand = "CeraVe", category = "Cleanser", imageUrl = "http://example.com/img.jpg")
        assertEquals("p1", product.id)
        assertEquals("CeraVe Cleanser", product.name)
        assertEquals("CeraVe", product.brand)
    }
}
