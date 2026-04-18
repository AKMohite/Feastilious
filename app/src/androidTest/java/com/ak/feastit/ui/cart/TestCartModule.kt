package com.ak.feastit.ui.cart

import com.ak.feastit.di.AppModule
import com.mak.feastit.data.di.RepositoryModule
import com.mak.feastit.domain.repository.CartRepository
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [RepositoryModule::class]
)
object TestRepositoryModule {
  @Provides
  @Singleton
  fun provideCartRepository(): CartRepository = FakeCartRepository()

  @Provides
  @Singleton
  fun provideRecipeRepository(): RecipeRepository = FakeRecipeRepository()

  @Provides
  @Singleton
  fun provideRecipesRepository(): RecipesRepository = FakeRecipesRepository()
}

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [AppModule::class]
)
object TestAppModule {
  @Provides
  @Singleton
  fun provideDispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
    override val io: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val computation: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val main: CoroutineDispatcher = UnconfinedTestDispatcher()
  }

  @Provides
  @Singleton
  @dagger.hilt.android.qualifiers.ApplicationContext
  fun provideApiKey(): String = "test_api_key"
}
