package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.json.JSONObject;

@Entity(nameInDb = "User")
public class User {


    public final static String COLUMN_USERUUID = "userUUID";
    public final static String COLUMN_USERNAME = "username";
    public final static String COLUMN_PASSWORD = "password";
    public final static String COLUMN_PROVIDER = "provider";


    @Property(nameInDb = COLUMN_USERUUID)
    private String userUUID;

    @Property(nameInDb = COLUMN_USERNAME)
    private String username;

    @Property(nameInDb = COLUMN_PASSWORD)
    private String password;

    @Property(nameInDb = COLUMN_PROVIDER)
    private String provider;


    @Id(autoincrement = true)
    private Long id;



    @Generated(hash = 586692638)
    public User() {
    }

    @Generated(hash = 46826704)
    public User(String userUUID, String username, String password, String provider,
            Long id) {
        this.userUUID = userUUID;
        this.username = username;
        this.password = password;
        this.provider = provider;
        this.id = id;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProvider() {
        return provider;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}