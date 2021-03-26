package com.smarttoni.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeTag(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "tag") var tag: String,
        @ColumnInfo(name = "recipe") var recipe: String

)