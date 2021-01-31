package com.smarttoni.models;

import com.google.gson.annotations.SerializedName;

public class Data {


    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    private String scope;

    private Details details;

    @SerializedName("token_type")
    private String tokenType;

    private int expiresIn;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public Details getDetails() {
        return details;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "access_token = '" + accessToken + '\'' +
                        ",refresh_token = '" + refreshToken + '\'' +
                        ",scope = '" + scope + '\'' +
                        ",details = '" + details + '\'' +
                        ",token_type = '" + tokenType + '\'' +
                        ",expires_in = '" + expiresIn + '\'' +
                        "}";
    }
}
