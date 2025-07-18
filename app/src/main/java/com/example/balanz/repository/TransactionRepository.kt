package com.example.balanz.repository

import com.example.balanz.data.dao.TransactionDao
import com.example.balanz.data.entity.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()
    val totalIncome: Flow<Double> = transactionDao.getTotalIncome()
    val totalExpense: Flow<Double> = transactionDao.getTotalExpense()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    fun getTransactionsByType(type: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(type)
    }
}