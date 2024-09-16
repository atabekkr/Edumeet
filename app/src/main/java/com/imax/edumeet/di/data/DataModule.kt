package com.imax.edumeet.di.data

import com.imax.edumeet.data.EdumeetApi
import com.imax.edumeet.data.TokenInterceptor
import com.imax.edumeet.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        interceptor: TokenInterceptor,
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(40, TimeUnit.SECONDS) // connect timeout
            .writeTimeout(40, TimeUnit.SECONDS) // write timeout
            .callTimeout(40, TimeUnit.SECONDS) // call timeout
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): EdumeetApi {
        return retrofit.create(EdumeetApi::class.java)
    }
}