package com.example.balanz.ui.transaction

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.balanz.R
import com.example.balanz.data.entity.Transaction
import com.example.balanz.databinding.DialogAddTransactionBinding
import com.example.balanz.model.Categories
import com.example.balanz.util.CurrencyUtils
import java.util.Calendar

class AddTransactionDialog(
    private val transaction: Transaction? = null,
    private val onSave: (Transaction) -> Unit
) : DialogFragment() {
    private var _binding: DialogAddTransactionBinding? = null
    private val binding get() = _binding!!
    private var selectedDate: Long = Calendar.getInstance().timeInMillis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CenteredDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), theme)
        _binding = DialogAddTransactionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        // Setup type spinner
        val types = arrayOf("Income", "Expense")
        binding.spinnerType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            types
        )

        // Setup category spinner
        updateCategorySpinner("Income")

        // Set OnItemSelectedListener for spinnerType
        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateCategorySpinner(types[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No action needed
            }
        }

        // Setup date picker
        val calendar = Calendar.getInstance()
        transaction?.date?.let { calendar.timeInMillis = it }

        binding.editDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedDate = calendar.timeInMillis
                    binding.editDate.setText(
                        "${day}/${month + 1}/$year"
                    )
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Pre-fill fields if editing
        transaction?.let {
            binding.editTitle.setText(it.title)
            binding.spinnerType.setSelection(if (it.type == "income") 0 else 1)
            binding.editAmount.setText(CurrencyUtils.formatCurrency(requireContext(), it.amount))
            binding.spinnerCategory.setSelection(
                (if (it.type == "income") Categories.incomeCategories else Categories.expenseCategories)
                    .indexOfFirst { cat -> cat.name == it.category }
            )
            binding.editDate.setText(
                android.text.format.DateFormat.format("dd/MM/yyyy", it.date).toString()
            )
            selectedDate = it.date
        }

        binding.buttonSave.setOnClickListener {
            if (validateInputs()) {
                val newTransaction = Transaction(
                    id = transaction?.id ?: 0,
                    title = binding.editTitle.text.toString(),
                    amount = binding.editAmount.text.toString().replace("[^0-9.]".toRegex(), "").toDouble(),
                    category = binding.spinnerCategory.selectedItem.toString(),
                    type = binding.spinnerType.selectedItem.toString().lowercase(),
                    date = selectedDate
                )
                onSave(newTransaction)
                dismiss()
            }
        }

        return dialog
    }

    private fun updateCategorySpinner(type: String) {
        val categories = if (type == "Income") {
            Categories.incomeCategories.map { it.name }
        } else {
            Categories.expenseCategories.map { it.name }
        }
        binding.spinnerCategory.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        if (binding.editTitle.text.isEmpty()) {
            binding.editTitle.error = "Title is required"
            isValid = false
        }
        if (binding.editAmount.text.isEmpty()) {
            binding.editAmount.error = "Amount is required"
            isValid = false
        } else {
            try {
                binding.editAmount.text.toString().replace("[^0-9.]".toRegex(), "").toDouble()
            } catch (e: NumberFormatException) {
                binding.editAmount.error = "Invalid amount"
                isValid = false
            }
        }
        if (binding.editDate.text.isEmpty()) {
            binding.editDate.error = "Date is required"
            isValid = false
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}