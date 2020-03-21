package com.ihsinformatics.dynamicformsgenerator.data.pojos;

public class ContactDetailsSendable {

    private String contactID;
    private String contactFirstName;
    private String contactFamilyName;
    private String contactAge;

    public ContactDetailsSendable(String contactID, String contactFirstName, String contactFamilyName, String contactAge) {
        this.contactID = contactID;
        this.contactFirstName = contactFirstName;
        this.contactFamilyName = contactFamilyName;
        this.contactAge = contactAge;
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
}
