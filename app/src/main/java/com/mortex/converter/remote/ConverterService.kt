package com.mortex.converter.remote

import com.mortex.converter.data.model.GetCurrencyRates
import com.mortex.converter.remote.URLConstants.GET_RATES
import retrofit2.Response
import retrofit2.http.*

interface ConverterService {


    @GET(GET_RATES)
    suspend fun  getRates(
        @Query("access_key") accessKey: String
    ):Response<GetCurrencyRates>

}