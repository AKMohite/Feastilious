package com.ak.feastit.di

import com.ak.feastit.BuildConfig
import com.ak.feastit.utils.AppDispatcher
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    @Named("FEAST_KEY")
    fun provideApiKey(): String = BuildConfig.API_KEY

    @Provides
    @Singleton
    fun provideDispatchers(): DispatcherProvider = AppDispatcher()

}