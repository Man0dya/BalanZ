package com.example.balanz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.balanz.data.AppDatabase
import com.example.balanz.data.entity.Transaction
import com.example.balanz.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    val allTransactions: LiveData<List<Transaction>>
    val totalIncome: LiveData<Double>
    val totalExpense: LiveData<Double>

    init {
        val transactionDao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        allTransactions = repository.allTransactions.asLiveData()
        totalIncome = repository.totalIncome.asLiveData()
        totalExpense = repository.totalExpense.asLiveData()
    }

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }

    fun update(transaction: Transaction) = viewModelScope.launch {
        repository.update(transaction)
    }

    fun delete(transaction: Transaction) = viewModelScope.launch {
        repository.delete(transaction)
    }

    fun getTransactionsByType(type: String): Flow<List<Transaction>> {
        return repository.getTransactionsByType(type)
    }
}