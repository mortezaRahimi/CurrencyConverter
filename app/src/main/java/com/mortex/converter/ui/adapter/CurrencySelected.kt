package com.mortex.converter.ui.adapter

import com.mortex.converter.data.model.CurrencyRate

interface CurrencySelected {
    fun sellCurrencySelected(currencyRate: CurrencyRate)
    fun buyCurrencySelected(currencyRate: CurrencyRate)
}