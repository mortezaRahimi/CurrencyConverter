package com.mortex.converter.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mortex.converter.BuildConfig
import com.mortex.converter.local.Constants
import com.mortex.converter.local.SessionManager
import com.mortex.converter.remote.URLConstants.BASE_URL
import com.mortex.converter.data.ConverterRepository
import com.mortex.converter.remote.ConverterRemoteDataSource
import com.mortex.converter.remote.ConverterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences(
            Constants.PREF_NAME, Context.MODE_PRIVATE
        )!!

    @Singleton
    @Provides
    fun provideSessionManager(preferences: SharedPreferences) =
        SessionManager(preferences)

    @Provides
    fun provideLoginService(retrofit: Retrofit): ConverterService =
        retrofit.create(ConverterService::class.java)

    @Singleton
    @Provides
    fun provideLoginRemoteDataSource(converterService: ConverterService) =
        ConverterRemoteDataSource(converterService)

    @Singleton
    @Provides
    fun provideLoginRepository(
        remoteDataSource: ConverterRemoteDataSource,
        sessionManager: SessionManager
    ) =
        ConverterRepository(remoteDataSource, sessionManager)




}