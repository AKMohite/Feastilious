package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "last_syncs",
    indices = [Index(name = "last_syncs_entity_type_and_id", value = ["entity_type", "entity_id"], unique = true)]
)
data class LastSyncEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "entity_type")
    val entityType: String,
    @ColumnInfo(name = "entity_id")
    val entityId: Long = 0L,
    @ColumnInfo(name = "last_synced_at")
    val lastSyncedAt: Instant
)
