package com.mortex.converter.data.model

data class GetCurrencyRates(
    val base: String,
    val date: String,
    val rates: HashMap<String, Double> = HashMap(),
    val success: Boolean,
    val timestamp: Int
)
