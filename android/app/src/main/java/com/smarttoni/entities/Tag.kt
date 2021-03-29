package com.smarttoni.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.smarttoni.models.Locales

@Entity
data class Tag @JvmOverloads constructor(
        @SerializedName("uuid") @PrimaryKey var id: String,
        var name: String,
        @Ignore var locales: List<Locales>? = null) {

}