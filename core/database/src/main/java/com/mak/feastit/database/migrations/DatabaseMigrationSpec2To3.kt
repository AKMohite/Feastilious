// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.migrations

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec

@DeleteColumn(
  tableName = "recipes",
  columnName = "img",
)
class DatabaseMigrationSpec2To3 : AutoMigrationSpec
