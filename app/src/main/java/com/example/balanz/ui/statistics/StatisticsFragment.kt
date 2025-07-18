package com.example.balanz.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balanz.data.entity.Transaction
import com.example.balanz.databinding.FragmentStatisticsBinding
import com.example.balanz.util.CurrencyUtils
import com.example.balanz.viewmodel.TransactionViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            updateCharts(transactions)
        }
    }

    private fun updateCharts(transactions: List<Transaction>) {
        // Expense Pie Chart
        val expenseSums = transactions
            .filter { it.type == "expense" }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

        val expensePieEntries = expenseSums.map { (category, amount) ->
            PieEntry(amount, category)
        }

        val expenseDataSet = PieDataSet(expensePieEntries, "Expenses by Category")
        expenseDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        expenseDataSet.valueTextSize = 12f
        expenseDataSet.setDrawValues(true)
        expenseDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return CurrencyUtils.formatCurrency(requireContext(), value.toDouble())
            }
        }

        val expensePieData = PieData(expenseDataSet)
        binding.expensePieChart.apply {
            data = expensePieData
            description.isEnabled = false
            setUsePercentValues(true)
            setEntryLabelTextSize(10f)
            animateY(1000)
            invalidate()
        }

        // Income Pie Chart
        val incomeSums = transactions
            .filter { it.type == "income" }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

        val incomePieEntries = incomeSums.map { (category, amount) ->
            PieEntry(amount, category)
        }

        val incomeDataSet = PieDataSet(incomePieEntries, "Income by Category")
        incomeDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        incomeDataSet.valueTextSize = 12f
        incomeDataSet.setDrawValues(true)
        incomeDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return CurrencyUtils.formatCurrency(requireContext(), value.toDouble())
            }
        }

        val incomePieData = PieData(incomeDataSet)
        binding.incomePieChart.apply {
            data = incomePieData
            description.isEnabled = false
            setUsePercentValues(true)
            setEntryLabelTextSize(10f)
            animateY(1000)
            invalidate()
        }

        // Prepare data for line charts (last 30 days)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val displayDateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Get last 30 days
        val days = mutableListOf<String>()
        val dayMillis = 24 * 60 * 60 * 1000L
        val endDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -29) // 30 days including today
        val startDate = calendar.timeInMillis

        for (i in 0..29) {
            val currentMillis = startDate + (i * dayMillis)
            days.add(dateFormat.format(Date(currentMillis)))
        }

        // Group transactions by date
        val incomeByDate = transactions
            .filter { it.type == "income" && it.date >= startDate && it.date <= endDate }
            .groupBy { dateFormat.format(Date(it.date)) }
            .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

        val expenseByDate = transactions
            .filter { it.type == "expense" && it.date >= startDate && it.date <= endDate }
            .groupBy { dateFormat.format(Date(it.date)) }
            .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

        // Income Line Chart
        val incomeLineEntries = days.mapIndexed { index, date ->
            Entry(index.toFloat(), incomeByDate[date] ?: 0f)
        }

        val incomeLineDataSet = LineDataSet(incomeLineEntries, "Daily Income")
        incomeLineDataSet.color = ColorTemplate.MATERIAL_COLORS[0]
        incomeLineDataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[0])
        incomeLineDataSet.lineWidth = 2f
        incomeLineDataSet.circleRadius = 4f
        incomeLineDataSet.setDrawValues(false)

        val incomeLineData = LineData(incomeLineDataSet)
        binding.incomeLineChart.apply {
            data = incomeLineData
            description.isEnabled = false
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return displayDateFormat.format(Date(startDate + (value.toLong() * dayMillis)))
                    }
                }
                granularity = 1f
                setLabelCount(6, true)
            }
            axisLeft.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return CurrencyUtils.formatCurrency(requireContext(), value.toDouble())
                    }
                }
            }
            axisRight.isEnabled = false
            animateX(1000)
            invalidate()
        }

        // Expense Line Chart
        val expenseLineEntries = days.mapIndexed { index, date ->
            Entry(index.toFloat(), expenseByDate[date] ?: 0f)
        }

        val expenseLineDataSet = LineDataSet(expenseLineEntries, "Daily Expense")
        expenseLineDataSet.color = ColorTemplate.MATERIAL_COLORS[1]
        expenseLineDataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[1])
        expenseLineDataSet.lineWidth = 2f
        expenseLineDataSet.circleRadius = 4f
        expenseLineDataSet.setDrawValues(false)

        val expenseLineData = LineData(expenseLineDataSet)
        binding.expenseLineChart.apply {
            data = expenseLineData
            description.isEnabled = false
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return displayDateFormat.format(Date(startDate + (value.toLong() * dayMillis)))
                    }
                }
                granularity = 1f
                setLabelCount(6, true)
            }
            axisLeft.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return CurrencyUtils.formatCurrency(requireContext(), value.toDouble())
                    }
                }
            }
            axisRight.isEnabled = false
            animateX(1000)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}