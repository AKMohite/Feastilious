// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// this didn't work ;P
internal class DBMigrationFrom1To2 : Migration(1, 2) {
  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL("DROP TABLE IF EXISTS `last_syncs`")

    db.execSQL(
      "CREATE TABLE IF NOT EXISTS `meal_planner` (`id` INTEGER NOT NULL, " +
        "`planned_for` TEXT, PRIMARY KEY(`id`), FOREIGN KEY(`id`) REFERENCES " +
        "`recipes`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )",
    )
    db.execSQL(
      "CREATE TABLE IF NOT EXISTS `last_syncs` (`id` INTEGER PRIMARY KEY " +
        "AUTOINCREMENT NOT NULL, `entity_type` TEXT NOT NULL, `entity_id` INTEGER " +
        "NOT NULL, `last_synced_at` TEXT NOT NULL)",
    )
  }
}
