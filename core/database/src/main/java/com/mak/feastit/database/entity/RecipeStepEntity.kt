package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipe_instructions",
    indices = [Index(value = ["step_id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipe_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeStepEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "step_id") // recipeId+stepNo
    val stepId: String,
    @ColumnInfo(name = "recipe_id")
    val recipeId: Long,
    @ColumnInfo(name = "step_no")
    val stepNo: Int,
    @ColumnInfo(name = "step_desc")
    val stepDescription: String,
    @ColumnInfo(name = "step_name")
    val stepName: String,
//    @ColumnInfo(name = DB_STEP_INGREDIENTS)
//    val stepIngredients: String,
//    @ColumnInfo(name = DB_STEP_EQUIPMENTS)
//    val stepEquipments: String
)