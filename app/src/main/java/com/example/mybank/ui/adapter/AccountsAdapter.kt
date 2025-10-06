package com.example.mybank.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Carousel
import androidx.recyclerview.widget.RecyclerView
import com.example.mybank.data.model.Account
import com.example.mybank.databinding.ItemAccountBinding

class AccountsAdapter : RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {
    private val items = arrayListOf<Account>()

    fun submitList(data: List<Account>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): AccountsViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AccountsViewHolder, position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class AccountsViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) = with(binding) {
            tvName.text = account.name
            val text = "${account.balance.toString()} ${account.currency}"
            tvBalance.text = text
        }
    }
}