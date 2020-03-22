package com.ihsinformatics.dynamicformsgenerator.data.pojos;

public class ContactDetailsSendable {

    private String contactID;
    private String contactFirstName;
    private String contactFamilyName;
    private String contactAge;

    private String gender;
    private String relation;

    public ContactDetailsSendable(String contactID, String contactFirstName, String contactFamilyName, String contactAge, String gender, String relation) {
        this.contactID = contactID;
        this.contactFirstName = contactFirstName;
        this.contactFamilyName = contactFamilyName;
        this.contactAge = contactAge;
        this.gender = gender;
        this.relation = relation;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getContactFamilyName() {
        return contactFamilyName;
    }

    public void setContactFamilyName(String contactFamilyName) {
        this.contactFamilyName = contactFamilyName;
    }

    public String getContactAge() {
        return contactAge;
    }

    public void setContactAge(String contactAge) {
        this.contactAge = contactAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
