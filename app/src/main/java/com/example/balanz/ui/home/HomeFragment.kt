package com.example.balanz.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balanz.databinding.FragmentHomeBinding
import com.example.balanz.util.CurrencyUtils
import com.example.balanz.viewmodel.TransactionViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransactionViewModel
    private lateinit var sharedPrefs: SharedPreferences
    private var preferenceListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        sharedPrefs = requireContext().getSharedPreferences("balanz_prefs", Context.MODE_PRIVATE)

        // Observe total income
        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            binding.textTotalIncome.text = CurrencyUtils.formatCurrency(requireContext(), income ?: 0.0)
            updateBarChart()
        }

        // Observe total expense
        viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
            binding.textTotalExpense.text = CurrencyUtils.formatCurrency(requireContext(), expense ?: 0.0)
            updateBarChart()
            updateBudgetProgress()
        }

        // Observe both for balance
        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
                val balance = (income ?: 0.0) - (expense ?: 0.0)
                binding.textTotalBalance.text = CurrencyUtils.formatCurrency(requireContext(), balance)
            }
        }

        // Listen for budget changes
        preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "monthly_budget") {
                updateBudgetProgress()
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceListener)

        // Initial budget progress update
        updateBudgetProgress()
    }

    private fun updateBarChart() {
        val income = viewModel.totalIncome.value?.toFloat() ?: 0f
        val expense = viewModel.totalExpense.value?.toFloat() ?: 0f

        // Create bar entries
        val entries = listOf(
            BarEntry(0f, income),
            BarEntry(1f, expense)
        )

        // Create dataset
        val dataSet = BarDataSet(entries, "Income vs Expense")
        dataSet.colors = listOf(
            android.graphics.Color.GREEN,
            android.graphics.Color.RED
        )
        dataSet.valueTextSize = 12f
        dataSet.setDrawValues(true)

        // Create bar data
        val barData = BarData(dataSet)
        barData.barWidth = 0.45f // Width of each bar (total 0.9f for two bars)

        // Configure chart
        binding.homeBarChart.apply {
            data = barData
            description.isEnabled = false
            setFitBars(true)

            // X-axis configuration
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return when (value.toInt()) {
                            0 -> "Income"
                            1 -> "Expense"
                            else -> ""
                        }
                    }
                }
                granularity = 1f
                setLabelCount(2, true)
            }

            // Y-axis configuration
            axisLeft.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return CurrencyUtils.formatCurrency(requireContext(), value.toDouble())
                    }
                }
                axisMinimum = 0f
            }
            axisRight.isEnabled = false

            animateY(1000)
            invalidate()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        preferenceListener?.let {
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(it)
        }
        _binding = null
    }
}