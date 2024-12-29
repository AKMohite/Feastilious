package com.mak.feastit.remote.di


import com.mak.feastit.remote.ErrorInterceptor
import com.mak.feastit.remote.BuildConfig
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "https://api.spoonacular.com"

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Singleton
    @Provides
    @Named("LoggingInterceptor")
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            BODY
        else
            NONE
    }

    @Singleton
    @Provides
    @Named("MockInterceptor")
    fun provideMockInterceptor(): Interceptor = com.mak.feastit.remote.MockInterceptor()

    @Singleton
    @Provides
    @Named("ErrorInterceptor")
    fun provideErrorInterceptor(
        @Named("FEAST_KEY") key: String
    ): Interceptor = ErrorInterceptor(key)

    @Singleton
    @Provides
    fun provideCallFactory(
        @Named("LoggingInterceptor") loggingInterceptor: Interceptor,
        @Named("MockInterceptor") mockInterceptor: Interceptor,
        @Named("FEAST_KEY") key: String
    ): Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder().addQueryParameter("apiKey", key).build()
                val request = original.newBuilder().url(url)
                chain.proceed(request.build())
            }
            .addInterceptor(loggingInterceptor)
//            .addInterceptor(mockInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Singleton
    @Provides
    fun provideMoshiConverter(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        callFactory: Call.Factory
    ): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(callFactory)
        .addConverterFactory(moshiConverterFactory)

    @Singleton
    @Provides
    fun provideFeastService(retrofit: Retrofit.Builder): com.mak.feastit.remote.FeastAPIService = retrofit
        .build()
        .create(com.mak.feastit.remote.FeastAPIService::class.java)

}