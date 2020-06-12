package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Admin on 4/25/2017.
 */
@Entity(nameInDb = "user_credentials")
public class UserCredentials {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_PROVIDER_UUID = "provider_uuid";

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    @Property(nameInDb = COLUMN_USER_NAME)
    private String username;
    @Property(nameInDb = COLUMN_USER_PASSWORD)
    private String password;
    @Property(nameInDb = COLUMN_GENDER)
    private String gender;
    @Property(nameInDb = COLUMN_FULL_NAME)
    private String fullName;
    @NotNull
    @Property(nameInDb = COLUMN_UUID)
    @Unique
    private String uuid;
    @Property(nameInDb = COLUMN_PROVIDER_UUID)
    private String providerUUID;

    public UserCredentials(String username, String password, String fullName, String gender, String uuid, String providerUUID){
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.fullName = fullName;
        this.uuid = uuid;
        this.providerUUID = providerUUID;
    }

    @Generated(hash = 1256379925)
    public UserCredentials(Long id, @NotNull String username, String password, String gender, String fullName, @NotNull String uuid,
            String providerUUID) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.fullName = fullName;
        this.uuid = uuid;
        this.providerUUID = providerUUID;
    }

    @Generated(hash = 1157038130)
    public UserCredentials() {
    }
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProviderUUID() {
        return providerUUID;
    }

    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
