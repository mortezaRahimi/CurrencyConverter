package com.mortex.converter.utils

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mortex.converter.data.model.BalanceItem


@BindingAdapter("setCurrencyAndAmount")
fun TextView.setCurrencyAndAmount(item: BalanceItem) {
    val value = item.currency + " " + item.amount.toString()
    this.text = value
}
