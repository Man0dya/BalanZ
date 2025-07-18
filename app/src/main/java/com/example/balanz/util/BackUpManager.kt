package com.example.balanz.util

import android.content.Context
import com.example.balanz.data.AppDatabase
import com.example.balanz.data.entity.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

object BackupManager {
    fun backupData(context: Context) {
        runBlocking(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(context)
            val transactions = database.transactionDao().getAllTransactions().first()
            val gson = Gson()
            val json = gson.toJson(transactions)

            val backupFile = File(context.filesDir, "balanz_backup.json")
            backupFile.writeText(json)
        }
    }

    fun restoreData(context: Context) {
        runBlocking(Dispatchers.IO) {
            val backupFile = File(context.filesDir, "balanz_backup.json")
            if (backupFile.exists()) {
                val json = backupFile.readText()
                val gson = Gson()
                val type = object : TypeToken<List<Transaction>>() {}.type
                val transactions: List<Transaction> = gson.fromJson(json, type)

                val database = AppDatabase.getDatabase(context)
                val dao = database.transactionDao()

                // Clear existing data
                dao.getAllTransactions().first().forEach { dao.delete(it) }

                // Insert restored data
                transactions.forEach { dao.insert(it) }
            }
        }
    }
}