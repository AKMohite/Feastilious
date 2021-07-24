package com.ak.feastit.di

import android.app.Application
import androidx.room.Room
import com.ak.feastit.data.local.FeastDatabase
import com.ak.feastit.data.local.RecipeDAO
import com.ak.feastit.data.local.base.RecipeDomainMapper
import com.ak.feastit.domain.datasource.IRecipeLocalDataSource
import com.ak.feastit.data.local.datasource.RecipeLocalDataSource
import com.ak.feastit.utils.FEAST_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideDataBase(
            app: Application
    ) = Room.databaseBuilder(app, FeastDatabase::class.java, FEAST_DB)
            .fallbackToDestructiveMigration() // TODO room migration
            .build()

    @Provides
    fun provideRecipeDAO(db: FeastDatabase): RecipeDAO = db.recipeDAO()

    @Provides
    fun provideDomainMapper(): RecipeDomainMapper = RecipeDomainMapper()

    @Provides
    fun provideRecipeLocalSource(
        feastDatabase: FeastDatabase,
        recipeDomainMapper: RecipeDomainMapper
    ): IRecipeLocalDataSource = RecipeLocalDataSource(feastDatabase, recipeDomainMapper)

}