package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface BaseDAO<Entity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Entity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<Entity>)

    @Upsert
    suspend fun upsert(entity: Entity)

    @Upsert
    suspend fun upsert(entities: List<Entity>)

    @Update
    suspend fun update(entity: Entity)

    @Update
    suspend fun update(entities: List<Entity>)

    @Delete
    suspend fun delete(entity: Entity)

}