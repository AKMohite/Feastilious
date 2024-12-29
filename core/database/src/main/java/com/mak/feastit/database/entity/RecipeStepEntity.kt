package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mak.feastit.database.util.Constants.DB_INSTRUCTION_TABLE
import com.mak.feastit.database.util.Constants.DB_RECIPE_ID
import com.mak.feastit.database.util.Constants.DB_STEP_DESC
import com.mak.feastit.database.util.Constants.DB_STEP_ID
import com.mak.feastit.database.util.Constants.DB_STEP_NAME
import com.mak.feastit.database.util.Constants.DB_STEP_NUMBER

@Entity(
        tableName = DB_INSTRUCTION_TABLE,
        indices = [Index(value = [DB_STEP_ID], unique = true)]
)
data class RecipeStepEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DB_STEP_ID) // recipeId+stepNo
    val stepId: String,
    @ColumnInfo(name = DB_RECIPE_ID)
    val recipeId: Long,
    @ColumnInfo(name = DB_STEP_NUMBER)
    val stepNo: Int,
    @ColumnInfo(name = DB_STEP_DESC)
    val stepDescription: String,
    @ColumnInfo(name = DB_STEP_NAME)
    val stepName: String,
//    @ColumnInfo(name = DB_STEP_INGREDIENTS)
//    val stepIngredients: String,
//    @ColumnInfo(name = DB_STEP_EQUIPMENTS)
//    val stepEquipments: String
)