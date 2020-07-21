package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.dynamicformsgenerator.data.database.history.Encounters;


public class FormEncounterMapper {

    @SerializedName("formEncounterId")
    private  String formEncounterId;

    @SerializedName("componentForm")
    private  ComponentForm componentForm;

    @SerializedName("encounter")
    private  Encounters encounters;

    public Encounters getEncounters() {
        return encounters;
    }

    public void setEncounters(Encounters encounters) {
        this.encounters = encounters;
    }

    public String getFormEncounterId() {
        return formEncounterId;
    }

    public void setFormEncounterId(String formEncounterId) {
        this.formEncounterId = formEncounterId;
    }

    public ComponentForm getComponentForm() {
        return componentForm;
    }

    public void setComponentForm(ComponentForm componentForm) {
        this.componentForm = componentForm;
    }
}
