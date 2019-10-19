package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Naveed Iqbal on 11/8/2017.
 * Email: h.naveediqbal@gmail.com
 */

@Entity
public class Location implements Serializable {
    public static final long serialVersionUID = 1l;

    @Override
    public String toString() {
        return getName();
    }

    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String uuid;
    private String name;
    private String country; // Country
    private String state_province; // Division
    private String country_district; // District
    private String city_village; // Upazilla
    private String address1; // House/ unit
    private String address2; // Street/ area
    private String address3; // Union
    private String address4;
    private String address5;
    private String address6; // Display name
    private Long parentLocation;
    private Boolean voided;
    @Transient
    private Date dateCreated;
    private String parentLocationUUID;
    @Transient
    private ArrayList<LocationTag> locationTags;
    @Transient
    ArrayList<LocationAttribute> locationAttributes;

    public ArrayList<LocationAttribute> getLocationAttributes() {
        return locationAttributes;
    }

    public void setLocationAttributes(ArrayList<LocationAttribute> locationAttributes) {
        this.locationAttributes = locationAttributes;
    }

    public ArrayList<LocationTag> getLocationTags() {
        return locationTags;
    }

    public void setLocationTags(ArrayList<LocationTag> locationTags) {
        this.locationTags = locationTags;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getParentLocationUUID() {
        return parentLocationUUID;
    }

    public void setParentLocationUUID(String parentLocationUUID) {
        this.parentLocationUUID = parentLocationUUID;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState_province() {
        return this.state_province;
    }

    public void setState_province(String state_province) {
        this.state_province = state_province;
    }

    public String getCountry_district() {
        return this.country_district;
    }

    public void setCountry_district(String country_district) {
        this.country_district = country_district;
    }

    public String getCity_village() {
        return this.city_village;
    }

    public void setCity_village(String city_village) {
        this.city_village = city_village;
    }

    public String getAddress1() {
        return this.address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return this.address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return this.address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress5() {
        return this.address5;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
    }

    public String getAddress6() {
        return this.address6;
    }

    public void setAddress6(String address6) {
        this.address6 = address6;
    }

    public Long getParentLocation() {
        return this.parentLocation;
    }

    public void setParentLocation(Long parentLocation) {
        this.parentLocation = parentLocation;
    }

    public Boolean getVoided() {
        return this.voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

    public Location(Long id, String uuid, String name, String country,
                    String state_province, String country_district, String city_village,
                    String address1, String address2, String address3, String address4,
                    String address5, String address6, String parentLocationUUID, Date dateCreated) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.country = country;
        this.state_province = state_province;
        this.country_district = country_district;
        this.city_village = city_village;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.address5 = address5;
        this.address6 = address6;
        this.parentLocationUUID = parentLocationUUID;
        this.dateCreated = dateCreated;
    }

    @Generated(hash = 891102973)
    public Location(Long id, String uuid, String name, String country, String state_province,
            String country_district, String city_village, String address1, String address2,
            String address3, String address4, String address5, String address6, Long parentLocation,
            Boolean voided, String parentLocationUUID) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.country = country;
        this.state_province = state_province;
        this.country_district = country_district;
        this.city_village = city_village;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.address5 = address5;
        this.address6 = address6;
        this.parentLocation = parentLocation;
        this.voided = voided;
        this.parentLocationUUID = parentLocationUUID;
    }

    @Generated(hash = 375979639)
    public Location() {
    }
}
