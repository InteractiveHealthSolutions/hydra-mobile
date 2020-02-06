package com.ihsinformatics.dynamicformsgenerator.common;


public class FormDetails{

    private Integer componentFormID;
    private String componentFormUUID;

    public FormDetails(Integer componentFormID, String componentFormUUID) {
        this.componentFormID = componentFormID;
        this.componentFormUUID = componentFormUUID;
    }

    public Integer getComponentFormID() {
        return componentFormID;
    }

    public void setComponentFormID(Integer componentFormID) {
        this.componentFormID = componentFormID;
    }

    public String getComponentFormUUID() {
        return componentFormUUID;
    }

    public void setComponentFormUUID(String componentFormUUID) {
        this.componentFormUUID = componentFormUUID;
    }
}