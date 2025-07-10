package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mak.feastit.database.entity.LastSyncEntity

@Dao
interface LastSyncDAO: BaseDAO<LastSyncEntity> {

    @Query("SELECT * FROM last_syncs WHERE entity_type = :entityType AND entity_id = :entityId")
    suspend fun getLastSync(entityType: String, entityId: Long = 0L): LastSyncEntity?

    @Query("DELETE FROM last_syncs WHERE entity_type = :entityType AND entity_id = :entityId")
    suspend fun deleteEntity(entityType: String, entityId: String?)

    @Query("DELETE FROM last_syncs WHERE last_synced_at <= datetime('now', '-' || :days || ' days')")
    suspend fun deleteSinceDays(days: Int)

}