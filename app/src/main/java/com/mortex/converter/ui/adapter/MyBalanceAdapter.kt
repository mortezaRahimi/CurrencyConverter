package com.mortex.converter.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mortex.converter.data.model.BalanceItem
import com.mortex.converter.databinding.BalanceItemBinding
import kotlin.random.Random


class MyBalanceAdapter : RecyclerView.Adapter<MyBalanceItemViewHolder>() {

    private val items = ArrayList<BalanceItem>()

    fun setItems(items: List<BalanceItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBalanceItemViewHolder {
        val binding: BalanceItemBinding =
            BalanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBalanceItemViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyBalanceItemViewHolder, position: Int) =
        holder.bind(items[position])
}

class MyBalanceItemViewHolder(
    private val itemBinding: BalanceItemBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var balanceItem: BalanceItem


    fun bind(item: BalanceItem) {
        this.balanceItem = item
        val value = item.currency + " " + balanceItem.amount.toString()
        itemBinding.balanceValue.text = value
    }


}

