package com.mak.feastit.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal class DBMigrationFrom1To2: Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `meal_planner` (`id` INTEGER NOT NULL, " +
                "`planned_for` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`id`) REFERENCES " +
                "`recipes`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )"
        )
    }
}