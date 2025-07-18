package com.example.balanz.model

data class Category(
    val name: String,
    val type: String // "income" or "expense"
)

object Categories {
    val incomeCategories = listOf(
        Category("Salary", "income"),
        Category("Freelance", "income"),
        Category("Investments", "income"),
        Category("Other Income", "income")
    )

    val expenseCategories = listOf(
        Category("Food", "expense"),
        Category("Transport", "expense"),
        Category("Bills", "expense"),
        Category("Entertainment", "expense"),
        Category("Other Expense", "expense")
    )
}