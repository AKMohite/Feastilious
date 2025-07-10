package com.mak.feastit.database.di

import android.app.Application
import androidx.room.Room
import com.mak.feastit.database.migrations.DBMigrationFrom1To2
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
    ): com.mak.feastit.database.FeastDB = Room.databaseBuilder(app, com.mak.feastit.database.FeastDatabase::class.java, "feast_db")
        .addMigrations(
            DBMigrationFrom1To2()
        )
        .build()

}