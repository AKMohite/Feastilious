package com.ak.feastit.di

import com.ak.feastit.BuildConfig
import com.ak.feastit.data.network.FeastAPIService
import com.ak.feastit.data.network.MockInterceptor
import com.ak.feastit.data.network.base.RecipeEntityMapper
import com.ak.feastit.data.network.datasource.RecipeNetworkSource
import com.ak.feastit.data.network.datasource.RecipeNetworkSourceImpl
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
const val API_KEY = BuildConfig.API_KEY

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

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
    fun provideMockInterceptor(): Interceptor = MockInterceptor()

    @Singleton
    @Provides
    fun provideCallFactory(
        @Named("LoggingInterceptor") loggingInterceptor: Interceptor,
        @Named("MockInterceptor") mockInterceptor: Interceptor
    ): Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder().addQueryParameter("apiKey", API_KEY).build()
                val request = original.newBuilder().url(url)
                chain.proceed(request.build())
            }
            .addInterceptor(loggingInterceptor)
            .addInterceptor(mockInterceptor)
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
    fun provideFeastService(retrofit: Retrofit.Builder): FeastAPIService = retrofit
        .build()
        .create(FeastAPIService::class.java)

    @Provides
    fun provideRecipeNetworkSource(
        apiService: FeastAPIService,
        recipeEntityMapper: RecipeEntityMapper,
    ): RecipeNetworkSource = RecipeNetworkSourceImpl(apiService, recipeEntityMapper)

}