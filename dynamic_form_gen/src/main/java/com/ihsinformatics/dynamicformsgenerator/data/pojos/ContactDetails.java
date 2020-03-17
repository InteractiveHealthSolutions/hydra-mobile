package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import java.util.List;

public class ContactDetails {

    private String questionText;
    private String questionNumber;

    private String contactID;
    private String contactName;
    private String contactAge;
    private String contactGender;
    private String contactRelationships;

    public ContactDetails(String questionText, String questionNumber, String contactID, String contactName, String contactAge, String contactGender, String contactRelationships) {
        this.questionText = questionText;
        this.questionNumber = questionNumber;

        this.contactID = contactID;
        this.contactName = contactName;
        this.contactAge = contactAge;
        this.contactGender = contactGender;
        this.contactRelationships = contactRelationships;
    }

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactAge() {
        return contactAge;
    }

    public void setContactAge(String contactAge) {
        this.contactAge = contactAge;
    }

    public String getContactGender() {
        return contactGender;
    }

    public void setContactGender(String contactGender) {
        this.contactGender = contactGender;
    }

    public String getContactRelationships() {
        return contactRelationships;
    }

    public void setContactRelationships(String contactRelationships) {
        this.contactRelationships = contactRelationships;
    }
}
