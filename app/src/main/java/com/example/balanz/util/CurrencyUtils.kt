package com.example.balanz.util

import android.content.Context
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyUtils {
    private const val PREFS_NAME = "balanz_prefs"
    private const val KEY_CURRENCY = "currency_code"
    private val DEFAULT_CURRENCY = "USD"

    fun getSelectedCurrency(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CURRENCY, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY
    }

    fun setSelectedCurrency(context: Context, currencyCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString(KEY_CURRENCY, currencyCode)
            apply()
        }
    }

    fun formatCurrency(context: Context, amount: Double): String {
        val currencyCode = getSelectedCurrency(context)
        val locale = when (currencyCode) {
            "USD" -> Locale.US
            "EUR" -> Locale.GERMANY
            "INR" -> Locale("en", "IN")
            "GBP" -> Locale.UK
            "JPY" -> Locale.JAPAN
            else -> Locale.US
        }
        val currency = Currency.getInstance(currencyCode)
        val formatter = NumberFormat.getCurrencyInstance(locale)
        formatter.currency = currency
        return formatter.format(amount)
    }
}