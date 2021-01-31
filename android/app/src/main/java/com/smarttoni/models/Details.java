package com.smarttoni.models;

import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("country_code")
    private String countryCode;

    private String city;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("first_name")
    private String firstName;

    private String uuid;

    private String email;

    @SerializedName("image_uuid")
    private String imageUuid;

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

//	public void setEnabled(boolean enabled){
//		this.enabled = enabled;
//	}
//
//	public boolean isEnabled(){
//		return enabled;
//	}

    public void setImageUuid(String imageUuid) {
        this.imageUuid = imageUuid;
    }

    public String getImageUuid() {
        return imageUuid;
    }

    @Override
    public String toString() {
        return
                "Details{" +
                        "country_code = '" + countryCode + '\'' +
                        ",city = '" + city + '\'' +
                        ",image_url = '" + imageUrl + '\'' +
                        ",last_name = '" + lastName + '\'' +
                        ",first_name = '" + firstName + '\'' +
                        ",uuid = '" + uuid + '\'' +
                        ",email = '" + email + '\'' +
//			",enabled = '" + enabled + '\'' +
                        ",image_uuid = '" + imageUuid + '\'' +
                        "}";
    }
}
