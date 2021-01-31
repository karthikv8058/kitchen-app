package com.smarttoni.server_app.models.UsersResponse

import com.google.gson.annotations.SerializedName

class Settings {
    @SerializedName("settings_key")
    var settingsKey: String? = null

    @SerializedName("settings_value")
    var settings_value: String? = null
}
