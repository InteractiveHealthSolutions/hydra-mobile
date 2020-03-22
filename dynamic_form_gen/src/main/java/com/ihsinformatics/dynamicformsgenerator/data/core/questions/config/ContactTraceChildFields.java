package com.ihsinformatics.dynamicformsgenerator.data.core.questions.config;

public class ContactTraceChildFields {

    private String field;
    private String displayText;
    private boolean mandatory;

    public ContactTraceChildFields(String field, String displayText, boolean mandatory) {
        this.field = field;
        this.displayText = displayText;
        this.mandatory = mandatory;
    }

    public String getField() {
        return field;
    }


    public String getDisplayText() {
        return displayText;
    }


    public boolean isMandatory() {
        return mandatory;
    }

}