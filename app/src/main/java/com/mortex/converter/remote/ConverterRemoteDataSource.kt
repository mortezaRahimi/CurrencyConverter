package com.mortex.converter.remote

import com.mortex.converter.base.BaseDataSource
import javax.inject.Inject

class ConverterRemoteDataSource @Inject constructor(
    private val converterService: ConverterService
) : BaseDataSource() {

    suspend fun signIn(token: String) =
        getResult { converterService.getRates(token) }

}