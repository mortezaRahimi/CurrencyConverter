package com.mortex.converter.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.mortex.converter.data.ConverterRepository

class ConverterViewModel @ViewModelInject constructor(
    private val converterRepository: ConverterRepository
) : ViewModel() {

    fun getRates() = converterRepository.signIn()

    fun getBalance() = converterRepository.getBalance()

    fun setBalance(amount: Double) = converterRepository.setBalance(amount)
}

