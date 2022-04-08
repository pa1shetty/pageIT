package com.example.pageit.di

import com.example.pageit.network.BeApiService
import com.example.pageit.network.FbApiService
import com.example.pageit.network.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val FB_BASE_URL = "https://graph.facebook.com/v13.0/"
    private const val BE_BASE_URL = "http://65.2.9.217:5000/"

    @Singleton
    @Provides
    fun providesOkHttpClient(networkConnectionInterceptor: NetworkConnectionInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(networkConnectionInterceptor)
            .build()

    @Singleton
    @Provides
    @Named("fbRetrofit")
    fun provideFbRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(FB_BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    @Named("beRetrofit")
    fun provideBeRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BE_BASE_URL)
        .client(okHttpClient)
        .build()


    @Singleton
    @Provides
    fun provideFbApiService(@Named("fbRetrofit") retrofit: Retrofit): FbApiService =
        retrofit.create(FbApiService::class.java)

    @Singleton
    @Provides
    fun provideBeApiService(@Named("beRetrofit") retrofit: Retrofit): BeApiService =
        retrofit.create(BeApiService::class.java)

}