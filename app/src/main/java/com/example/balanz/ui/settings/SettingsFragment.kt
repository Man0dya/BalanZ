package com.example.balanz.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balanz.R
import com.example.balanz.databinding.FragmentSettingsBinding
import com.example.balanz.util.BackupManager
import com.example.balanz.util.CurrencyUtils
import com.example.balanz.util.NotificationHelper
import com.example.balanz.viewmodel.TransactionViewModel
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransactionViewModel
    private lateinit var sharedPrefs: SharedPreferences
    private var preferenceListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        sharedPrefs = requireContext().getSharedPreferences("balanz_prefs", Context.MODE_PRIVATE)

        // Initialize notification channel
        NotificationHelper.createNotificationChannel(requireContext())

        // Setup currency spinner
        val currencies = arrayOf("USD", "EUR", "INR", "GBP", "JPY", "LKR")
        binding.spinnerCurrency.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            currencies
        )
        val savedCurrency = CurrencyUtils.getSelectedCurrency(requireContext())
        binding.spinnerCurrency.setSelection(currencies.indexOf(savedCurrency))

        // Load saved preferences
        val savedBudget = sharedPrefs.getFloat("monthly_budget", 0f)
        binding.editBudget.setText(
            if (savedBudget > 0) CurrencyUtils.formatCurrency(requireContext(), savedBudget.toDouble()) else ""
        )

        // Setup theme toggle
        val savedTheme = sharedPrefs.getString("theme_mode", "system") ?: "system"
        binding.switchTheme.isChecked = savedTheme == "dark"
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val newTheme = if (isChecked) "dark" else "light"
            with(sharedPrefs.edit()) {
                putString("theme_mode", newTheme)
                apply()
            }
            // Apply theme without any PIN-related checks
            AppCompatDelegate.setDefaultNightMode(
                when (newTheme) {
                    "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                    "light" -> AppCompatDelegate.MODE_NIGHT_NO
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }

        // Setup PIN change
        binding.buttonChangePin.setOnClickListener {
            ChangePinDialog().show(childFragmentManager, "ChangePinDialog")
        }

        // Listen for budget changes
        preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "monthly_budget") {
                // Reset notification flags when budget changes
                with(sharedPrefs.edit()) {
                    putBoolean("budget_near_notified", false)
                    putBoolean("budget_exceeded_notified", false)
                    apply()
                }
                updateBudgetProgress()
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceListener)

        // Observe total expense
        viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
            // Reset notification flags if expense decreases significantly
            val currentExpense = expense ?: 0.0
            val previousExpense = sharedPrefs.getFloat("last_expense", 0f).toDouble()
            if (currentExpense < previousExpense) {
                with(sharedPrefs.edit()) {
                    putBoolean("budget_near_notified", false)
                    putBoolean("budget_exceeded_notified", false)
                    apply()
                }
            }
            with(sharedPrefs.edit()) {
                putFloat("last_expense", currentExpense.toFloat())
                apply()
            }
            updateBudgetProgress()
        }

        // Initial budget progress update
        updateBudgetProgress()

        binding.buttonSaveSettings.setOnClickListener {
            val budgetText = binding.editBudget.text.toString().replace("[^0-9.]".toRegex(), "")
            try {
                val budget = budgetText.toFloat()
                with(sharedPrefs.edit()) {
                    putFloat("monthly_budget", budget)
                    apply()
                }
                // Save currency
                val selectedCurrency = binding.spinnerCurrency.selectedItem.toString()
                CurrencyUtils.setSelectedCurrency(requireContext(), selectedCurrency)
                Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()
            } catch (e: NumberFormatException) {
                binding.editBudget.error = "Invalid budget amount"
            }
        }

        binding.buttonBackup.setOnClickListener {
            BackupManager.backupData(requireContext())
            Toast.makeText(context, "Backup created", Toast.LENGTH_SHORT).show()
        }

        binding.buttonRestore.setOnClickListener {
            BackupManager.restoreData(requireContext())
            Toast.makeText(context, "Data restored", Toast.LENGTH_SHORT).show()
        }

        binding.buttonExportPdf.setOnClickListener {
            viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
                try {
                    val file = requireContext().filesDir.resolve("transactions_history.pdf")
                    val writer = PdfWriter(file)
                    val pdf = PdfDocument(writer)
                    val document = Document(pdf)

                    // Add title
                    document.add(
                        Paragraph("Transaction History")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(20f)
                            .setBold()
                    )

                    // Create table
                    val table = Table(floatArrayOf(50f, 100f, 80f, 100f, 80f, 80f))
                    table.addHeaderCell("ID")
                    table.addHeaderCell("Title")
                    table.addHeaderCell("Amount")
                    table.addHeaderCell("Category")
                    table.addHeaderCell("Type")
                    table.addHeaderCell("Date")

                    // Add transactions to table
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    transactions.forEach { transaction ->
                        table.addCell(transaction.id.toString())
                        table.addCell(transaction.title)
                        table.addCell(CurrencyUtils.formatCurrency(requireContext(), transaction.amount))
                        table.addCell(transaction.category)
                        table.addCell(transaction.type)
                        table.addCell(dateFormat.format(Date(transaction.date)))
                    }

                    document.add(table)
                    document.close()

                    Toast.makeText(
                        context,
                        "PDF saved to ${file.absolutePath}",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateBudgetProgress() {
        val budget = sharedPrefs.getFloat("monthly_budget", 0f)
        val expense = viewModel.totalExpense.value ?: 0.0

        if (budget <= 0) {
            binding.progressBudget.visibility = View.GONE
            binding.textBudgetProgress.visibility = View.GONE
            binding.textBudgetDetails.visibility = View.GONE
            binding.textBudgetProgressLabel.visibility = View.GONE
            return
        }

        binding.progressBudget.visibility = View.VISIBLE
        binding.textBudgetProgress.visibility = View.VISIBLE
        binding.textBudgetDetails.visibility = View.VISIBLE
        binding.textBudgetProgressLabel.visibility = View.VISIBLE

        val progress = if (budget > 0) {
            ((expense / budget) * 100).coerceAtMost(100.0).toInt()
        } else {
            0
        }

        binding.progressBudget.progress = progress
        binding.textBudgetProgress.text = "$progress%"

        // Set progress bar color
        val progressTint = when {
            progress < 50 -> ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
            progress <= 80 -> ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark)
            else -> ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
        }
        binding.progressBudget.progressTintList = android.content.res.ColorStateList.valueOf(progressTint)

        // Update budget details
        binding.textBudgetDetails.text = "${CurrencyUtils.formatCurrency(requireContext(), expense)} / ${CurrencyUtils.formatCurrency(requireContext(), budget.toDouble())}"

        // Trigger notifications for budget thresholds
        val nearNotified = sharedPrefs.getBoolean("budget_near_notified", false)
        val exceededNotified = sharedPrefs.getBoolean("budget_exceeded_notified", false)

        if (progress >= 80 && progress < 100 && !nearNotified) {
            NotificationHelper.showBudgetNotification(
                requireContext(),
                getString(R.string.notification_budget_near),
                getString(R.string.message_budget_near),
                1 // Notification ID for "near" threshold
            )
            with(sharedPrefs.edit()) {
                putBoolean("budget_near_notified", true)
                apply()
            }
        }

        if (progress >= 100 && !exceededNotified) {
            NotificationHelper.showBudgetNotification(
                requireContext(),
                getString(R.string.notification_budget_exceeded),
                getString(R.string.message_budget_exceeded),
                2 // Notification ID for "exceeded" threshold
            )
            with(sharedPrefs.edit()) {
                putBoolean("budget_exceeded_notified", true)
                apply()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        preferenceListener?.let {
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(it)
        }
        _binding = null
    }
}