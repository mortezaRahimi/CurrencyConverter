package com.mortex.converter.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mortex.converter.data.model.CurrencyRate
import com.mortex.converter.databinding.CurrencyRateItemBinding


class CurrencyRateAdapter(currencySelected: CurrencySelected, isForSell: Boolean) :
    RecyclerView.Adapter<CurrencyRateViewHolder>() {

    private val items = ArrayList<CurrencyRate>()
    private val listener = currencySelected
    private val isForSell = isForSell


    fun setItems(items: List<CurrencyRate>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateViewHolder {
        val binding: CurrencyRateItemBinding =
            CurrencyRateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyRateViewHolder(binding, listener, isForSell)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CurrencyRateViewHolder, position: Int) =
        holder.bind(items[position])
}

class CurrencyRateViewHolder(
    private val itemBinding: CurrencyRateItemBinding,
    private val listener: CurrencySelected,
    private val isForSell: Boolean
) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

    init {
        itemBinding.root.setOnClickListener(this)
    }

    private lateinit var currencyRate: CurrencyRate


    fun bind(item: CurrencyRate) {
        this.currencyRate = item
        val value = item.currency
        itemBinding.currencyRate.text = value

    }

    override fun onClick(view: View?) {
        if (isForSell)
            listener.sellCurrencySelected(currencyRate)
        else
            listener.buyCurrencySelected(currencyRate)
    }


}

