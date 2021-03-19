package com.ak.feastit.data.local.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDAO<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(entity: T)

    @Update
    suspend fun updateEntity(entity: T)

    @Delete
    suspend fun deleteEntity(entity: T)
}