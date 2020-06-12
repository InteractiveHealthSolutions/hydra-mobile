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

public class LocationDTO implements Serializable {
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
    private String stateProvince; // Division
    private String countryDistrict; // District
    private String cityVillage; // Upazilla
    private String address1; // House/ unit
    private String address2; // Street/ area
    private String address3; // Union
    private String address4;
    private String address5;
    private String address6; // Display name

    private LocationDTO parentLocation;
    private Boolean voided;

    private String parentLocationUUID;
    private ArrayList<LocationTag> tags;
    ArrayList<LocationAttribute> locationAttributes;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountryDistrict() {
        return countryDistrict;
    }

    public void setCountryDistrict(String countryDistrict) {
        this.countryDistrict = countryDistrict;
    }

    public String getCityVillage() {
        return cityVillage;
    }

    public void setCityVillage(String cityVillage) {
        this.cityVillage = cityVillage;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress5() {
        return address5;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
    }

    public String getAddress6() {
        return address6;
    }

    public void setAddress6(String address6) {
        this.address6 = address6;
    }

    public LocationDTO getParentLocation() {
        return parentLocation;
    }

    public void setParentLocation(LocationDTO parentLocation) {
        this.parentLocation = parentLocation;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

    public String getParentLocationUUID() {
        return parentLocationUUID;
    }

    public void setParentLocationUUID(String parentLocationUUID) {
        this.parentLocationUUID = parentLocationUUID;
    }

    public ArrayList<LocationTag> getLocationTags() {
        return tags;
    }

    public void setLocationTags(ArrayList<LocationTag> locationTags) {
        this.tags = locationTags;
    }

    public ArrayList<LocationAttribute> getLocationAttributes() {
        return locationAttributes;
    }

    public void setLocationAttributes(ArrayList<LocationAttribute> locationAttributes) {
        this.locationAttributes = locationAttributes;
    }
}
