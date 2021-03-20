package com.ak.feastit.data.local

import androidx.room.*
import com.ak.feastit.utils.*

@Entity(
        tableName = DB_INSTRUCTION_TABLE,
        foreignKeys = [ForeignKey(
                entity = RecipeEntity::class,
                parentColumns = [DB_TABLE_ID],
                childColumns = [DB_RECIPE_ID],
                onDelete = ForeignKey.CASCADE
        )]
)
data class InstructionEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DB_STEP_ID) // recipeId+stepNo
    val stepId: String,
    @ColumnInfo(name = DB_RECIPE_ID)
    val recipeId: Long,
    @ColumnInfo(name = DB_STEP_NUMBER)
    val stepNo: Int,
    @ColumnInfo(name = DB_STEP_DESC)
    val stepDesc: String,
    @ColumnInfo(name = DB_STEP_INGREDIENTS)
    val stepIngredients: String,
    @ColumnInfo(name = DB_STEP_EQUIPMENTS)
    val stepEquipments: String,
)