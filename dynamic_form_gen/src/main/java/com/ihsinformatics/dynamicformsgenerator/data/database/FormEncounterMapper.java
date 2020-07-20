package com.ihsinformatics.dynamicformsgenerator.data.database;

import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.dynamicformsgenerator.data.database.history.Encounters;

import java.util.ArrayList;

public class FormEncounterMapper {

    @SerializedName("encounter")
    Encounters encounters;

    public Encounters getEncounters() {
        return encounters;
    }

    public void setEncounters(Encounters encounters) {
        this.encounters = encounters;
    }
}
