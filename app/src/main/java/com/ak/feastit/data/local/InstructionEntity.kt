package com.ak.feastit.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ak.feastit.utils.*

@Entity(
        tableName = DB_INSTRUCTION_TABLE,
        indices = [Index(value = [DB_STEP_ID], unique = true)]
)
data class InstructionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DB_TABLE_ID)
    val id: Long,
    @ColumnInfo(name = DB_RECIPE_ID)
    val recipeId: Long,
    @ColumnInfo(name = DB_STEP_ID) // recipeId+stepNo
    val stepId: String,
    @ColumnInfo(name = DB_STEP_NUMBER)
    val stepNo: Int,
    @ColumnInfo(name = DB_STEP_DESC)
    val stepDesc: String,
    @ColumnInfo(name = DB_STEP_INGREDIENTS)
    val stepIngredients: String,
    @ColumnInfo(name = DB_STEP_EQUIPMENTS)
    val stepEquipments: String,
)