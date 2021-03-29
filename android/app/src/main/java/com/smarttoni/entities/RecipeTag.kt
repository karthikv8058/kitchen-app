package com.smarttoni.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.smarttoni.models.Locales
import com.smarttoni.models.TagValues

@Entity
data class RecipeTag @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "tag") var tag: String,
        @ColumnInfo(name = "recipe") var recipe: String,
        @ColumnInfo(name = "canOverride") var canOverride: Boolean,
        @ColumnInfo(name = "value") var value: String,
        @Ignore var meta: TagValues? = null
        ){
}
