package com.ihsinformatics.dynamicformsgenerator.data.database.history;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "service_history")
public class ServiceHistory {

    public final static String COLUMN_FORM_PATIENT_ID = "patientIdentifier";
    public final static String ENCOUNTER = "encounter";



    @Id
    @NotNull
    @Property(nameInDb = COLUMN_FORM_PATIENT_ID)
    private String patientIdentifier;


    @NotNull
    @Property(nameInDb = ENCOUNTER)
    private String encounter;


    @Generated(hash = 690705617)
    public ServiceHistory(@NotNull String patientIdentifier,
            @NotNull String encounter) {
        this.patientIdentifier = patientIdentifier;
        this.encounter = encounter;
    }


    @Generated(hash = 1106755047)
    public ServiceHistory() {
    }


    public String getPatientIdentifier() {
        return this.patientIdentifier;
    }


    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }


    public String getEncounter() {
        return this.encounter;
    }


    public void setEncounter(String encounter) {
        this.encounter = encounter;
    }


}
