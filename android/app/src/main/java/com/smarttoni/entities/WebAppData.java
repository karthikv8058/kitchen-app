package com.smarttoni.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "WebAppData")
public class WebAppData {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "access_token")
    private String accessToken;

    @Property(nameInDb = "profile_uuid")
    private String profileUuid;

    @Property(nameInDb = "detail")
    private String details;

    @Property(nameInDb = "refresh_token")
    private String refreshToken;

    @Property(nameInDb = "user_id")
    private String userId;

    @Property(nameInDb = "resturant_id")
    private String resturantId;

    @Generated(hash = 96912959)
    public WebAppData(Long id, String accessToken, String profileUuid,
                      String details, String refreshToken, String userId,
                      String resturantId) {
        this.id = id;
        this.accessToken = accessToken;
        this.profileUuid = profileUuid;
        this.details = details;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.resturantId = resturantId;
    }

    @Generated(hash = 1323293684)
    public WebAppData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getProfileUuid() {
        return this.profileUuid;
    }

    public void setProfileUuid(String profileUuid) {
        this.profileUuid = profileUuid;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResturantId() {
        return this.resturantId;
    }

    public void setResturantId(String resturantId) {
        this.resturantId = resturantId;
    }

}
