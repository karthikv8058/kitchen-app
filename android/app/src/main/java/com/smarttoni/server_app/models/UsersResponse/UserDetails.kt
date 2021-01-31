package com.smarttoni.server_app.models.UsersResponse

import com.google.gson.annotations.SerializedName

internal class UserDetails {
    @SerializedName("email")
    var email: String? = null

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null

    @SerializedName("country_code")
    var countryCode: String? = null

    @SerializedName("uuid")
    var uuid: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("settings")
    val setting: List<Settings>? = null
}
