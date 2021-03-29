package com.smarttoni.models

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

class TagValues {
    @SerializedName("override")
    var canOverride:Boolean = false

    @SerializedName("default")
    var value: String = ""
}