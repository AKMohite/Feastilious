package com.mak.feastit.database.di

import android.app.Application
import androidx.room.Room
import com.mak.feastit.database.util.Constants.FEAST_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalModule {

    @Singleton
    @Provides
    fun provideDataBase(
        app: Application
    ): com.mak.feastit.database.FeastDB = Room.databaseBuilder(app, com.mak.feastit.database.FeastDatabase::class.java, FEAST_DB)
        .fallbackToDestructiveMigration() // TODO room migration
        .build()

    @Provides
    fun provideRecipeLocalSource(
        feastDatabase: com.mak.feastit.database.FeastDB
    ): com.mak.feastit.database.datasource.IRecipeLocalDataSource =
        com.mak.feastit.database.datasource.RecipeLocalDataSource(feastDatabase)

}