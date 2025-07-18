package com.example.balanz

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.balanz.util.NotificationHelper
import com.example.balanz.databinding.ActivityPinBinding
import java.security.MessageDigest

class PinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinBinding
    private val TAG = "PinActivity"
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply theme before setContentView
        val sharedPrefs = getSharedPreferences("balanz_prefs", Context.MODE_PRIVATE)
        val themeMode = sharedPrefs.getString("theme_mode", "system") ?: "system"
        AppCompatDelegate.setDefaultNightMode(
            when (themeMode) {
                "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                "light" -> AppCompatDelegate.MODE_NIGHT_NO
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )

        super.onCreate(savedInstanceState)
        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create notification channel early
        NotificationHelper.createNotificationChannel(this)

        // Request notification permission for Android 13+
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        // Setup EncryptedSharedPreferences
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPrefsSecure = EncryptedSharedPreferences.create(
            "secure_prefs",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // Check if PIN is set
        val savedPinHash = sharedPrefsSecure.getString("pin_hash", null)
        Log.d(TAG, "Saved PIN hash: $savedPinHash")
        if (savedPinHash == null) {
            binding.textPinPrompt.text = "Set a 6-digit PIN"
            binding.buttonSubmitPin.text = "Set PIN"
        } else {
            binding.textPinPrompt.text = "Enter your 6-digit PIN"
            binding.buttonSubmitPin.text = "Submit"
        }

        binding.buttonSubmitPin.setOnClickListener {
            val enteredPin = binding.editPin.text.toString()
            if (enteredPin.length != 6 || !enteredPin.all { it.isDigit() }) {
                binding.editPin.error = "Enter a 6-digit PIN"
                Log.d(TAG, "Invalid PIN: $enteredPin")
                return@setOnClickListener
            }

            val enteredPinHash = enteredPin.toSHA256()
            Log.d(TAG, "Entered PIN hash: $enteredPinHash")
            if (savedPinHash == null) {
                // Set new PIN
                with(sharedPrefsSecure.edit()) {
                    putString("pin_hash", enteredPinHash)
                    apply()
                }
                Log.d(TAG, "New PIN set")
                startMainActivity()
            } else if (enteredPinHash == savedPinHash) {
                // Correct PIN
                Log.d(TAG, "PIN verified successfully")
                startMainActivity()
            } else {
                // Incorrect PIN
                binding.editPin.error = "Incorrect PIN"
                binding.editPin.text?.clear()
                Log.d(TAG, "Incorrect PIN entered")
            }
        }
    }

    private fun startMainActivity() {
        // Mark PIN as verified
        val sharedPrefs = getSharedPreferences("balanz_prefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putBoolean("is_pin_verified", true)
            apply()
        }
        Log.d(TAG, "Starting MainActivity, PIN verified")

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun String.toSHA256(): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}