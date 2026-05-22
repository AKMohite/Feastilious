// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.di

import coil3.intercept.Interceptor
import com.ak.feastit.compose.BuildConfig
import com.ak.feastit.compose.core.common.RecipeImageInterceptor
import com.ak.feastit.compose.core.support.PowerController
import com.ak.feastit.compose.core.support.RealPowerController
import com.ak.feastit.compose.utils.AppDispatcher
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.Binds
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

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppModuleBindings {

  @Binds
  abstract fun bindImageInterceptor(interceptor: RecipeImageInterceptor): Interceptor

  @Binds
  abstract fun bindPowerController(controller: RealPowerController): PowerController
}
