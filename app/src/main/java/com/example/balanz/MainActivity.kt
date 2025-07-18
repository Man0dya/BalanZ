package com.example.balanz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.balanz.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        // Check if PIN is set and not verified
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val securePrefs = EncryptedSharedPreferences.create(
            "secure_prefs",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val isPinVerified = sharedPrefs.getBoolean("is_pin_verified", false)
        if (securePrefs.contains("pin_hash") && !isPinVerified) {
            val intent = Intent(this, PinActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Proceed with MainActivity setup
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }

    override fun onPause() {
        super.onPause()
        // Reset PIN verification on pause (requires PIN on resume)
        val sharedPrefs = getSharedPreferences("balanz_prefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putBoolean("is_pin_verified", false)
            apply()
        }
    }
}