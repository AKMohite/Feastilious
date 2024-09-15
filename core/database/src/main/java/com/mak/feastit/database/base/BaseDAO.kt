package com.mak.feastit.database.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

internal interface BaseDAO<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(entity: T)

    @Update
    suspend fun updateEntity(entity: T)

    @Delete
    suspend fun deleteEntity(entity: T)
}