package com.mortex.converter.data

import com.mortex.converter.local.SessionManager
import com.mortex.converter.remote.ConverterRemoteDataSource
import com.mortex.converter.utils.performGetOperation
import javax.inject.Inject

class ConverterRepository @Inject constructor(
    private val converterRemoteDataSource: ConverterRemoteDataSource,
    private val sessionManager: SessionManager
) {

    fun signIn() = performGetOperation {
        converterRemoteDataSource.signIn("1a8bc0f8c105d7ba5a2f70add2342b9c")
    }

    fun getBalance() : Double? {
        return sessionManager.getBalance()?.toDouble()
    }

    fun setBalance(amount: Double) {
        sessionManager.setBalance(amount.toString())
    }


}