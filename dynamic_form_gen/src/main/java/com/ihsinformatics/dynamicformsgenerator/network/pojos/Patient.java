package com.ihsinformatics.dynamicformsgenerator.network.pojos;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Naveed Iqbal on 11/23/2017.
 * Email: h.naveediqbal@gmail.com
 */

public class Patient implements Serializable {

    private String identifier;
    private String givenName;
    private String familyName;
    private String uuid;
    private int age;
    private Date birthDate;
    private String gender;
    private int locationId;

    public Patient(String identifier, String givenName, String familyName, String uuid, int age, Date birthDate, String gender, int locationId) {
        this.identifier = identifier;
        this.givenName = givenName;
        this.familyName = familyName;
        this.uuid = uuid;
        this.age = age;
        this.birthDate = birthDate;
        this.gender = gender;
        this.locationId = locationId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        if (!identifier.equals(patient.identifier)) return false;
        return uuid.equals(patient.uuid);

    }

    @Override
    public int hashCode() {
        int result = identifier.hashCode();
        result = 31 * result + uuid.hashCode();
        return result;
    }
}
