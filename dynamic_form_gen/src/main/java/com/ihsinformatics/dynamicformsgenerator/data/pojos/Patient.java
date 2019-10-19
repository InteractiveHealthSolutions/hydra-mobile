package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Nabil shafi on 2/25/2019.
 */

@Entity(nameInDb = "patient")
public class Patient {

    @Id(autoincrement = true)
    private long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private Long dob;
    @Generated(hash = 36042588)
    public Patient(long id, String firstName, String lastName, String middleName,
            String gender, Long dob) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.gender = gender;
        this.dob = dob;
    }
    @Generated(hash = 1655646460)
    public Patient() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getMiddleName() {
        return this.middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public Long getDob() {
        return this.dob;
    }
    public void setDob(Long dob) {
        this.dob = dob;
    }

}
