package com.example.balanz.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.balanz.databinding.DialogChangePinBinding
import java.security.MessageDigest

class ChangePinDialog : DialogFragment() {
    private var _binding: DialogChangePinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangePinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSavePin.setOnClickListener {
            val newPin = binding.editNewPin.text.toString()
            val confirmPin = binding.editConfirmPin.text.toString()

            when {
                newPin.length != 6 || !newPin.all { it.isDigit() } -> {
                    binding.editNewPin.error = "Enter a 6-digit PIN"
                }
                confirmPin != newPin -> {
                    binding.editConfirmPin.error = "PINs do not match"
                }
                else -> {
                    // Save PIN to EncryptedSharedPreferences
                    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
                    val sharedPrefs = EncryptedSharedPreferences.create(
                        "secure_prefs",
                        masterKeyAlias,
                        requireContext(),
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )
                    with(sharedPrefs.edit()) {
                        putString("pin_hash", newPin.toSHA256())
                        apply()
                    }
                    Toast.makeText(context, "PIN changed successfully", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }

        binding.buttonCancelPin.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun String.toSHA256(): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}