package com.example.balanz.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balanz.data.entity.Transaction
import com.example.balanz.databinding.FragmentTransactionBinding
import com.example.balanz.viewmodel.TransactionViewModel

class TransactionFragment : Fragment() {
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        adapter = TransactionAdapter(
            onEdit = { transaction ->
                AddTransactionDialog(transaction) { updatedTransaction ->
                    viewModel.update(updatedTransaction)
                }.show(parentFragmentManager, "AddTransactionDialog")
            },
            onDelete = { transaction ->
                viewModel.delete(transaction)
            }
        )

        binding.recyclerTransactions.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = this@TransactionFragment.adapter
        }

        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            adapter.submitList(transactions.sortedByDescending { it.date })
        }

        binding.buttonAddTransaction.setOnClickListener {
            AddTransactionDialog { newTransaction ->
                viewModel.insert(newTransaction)
            }.show(parentFragmentManager, "AddTransactionDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}