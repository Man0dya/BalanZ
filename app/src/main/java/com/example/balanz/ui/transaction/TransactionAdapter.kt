package com.example.balanz.ui.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.balanz.data.entity.Transaction
import com.example.balanz.databinding.TransactionItemBinding
import com.example.balanz.util.CurrencyUtils
import java.text.SimpleDateFormat
import java.util.Date

class TransactionAdapter(
    private val onEdit: (Transaction) -> Unit,
    private val onDelete: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = TransactionItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TransactionViewHolder(
        private val binding: TransactionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.textTitle.text = transaction.title
            binding.textCategory.text = transaction.category
            binding.textDate.text = SimpleDateFormat("dd/MM/yyyy").format(Date(transaction.date))
            binding.textAmount.text = CurrencyUtils.formatCurrency(itemView.context, transaction.amount)
            binding.buttonEdit.setOnClickListener { onEdit(transaction) }
            binding.buttonDelete.setOnClickListener { onDelete(transaction) }
        }
    }
}

class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}