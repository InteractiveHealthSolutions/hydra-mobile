package com.ihsinformatics.dynamicformsgenerator.data.database.history;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "history")
public class ServiceHistory {

    public final static String COLUMN_FORM_PATIENT_ID = "patientIdentifier";
    public final static String ENCOUNTER = "encounter";
    public final static String COLUMN_FORM_OBS = "obs";


    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Property(nameInDb = COLUMN_FORM_PATIENT_ID)
    private String patientIdentifier;


    @NotNull
    @Property(nameInDb = ENCOUNTER)
    private String encounter;


    @Property(nameInDb = COLUMN_FORM_OBS)
    private String obs;


    @Generated(hash = 618936971)
    public ServiceHistory(Long id, @NotNull String patientIdentifier,
            @NotNull String encounter, String obs) {
        this.id = id;
        this.patientIdentifier = patientIdentifier;
        this.encounter = encounter;
        this.obs = obs;
    }


    @Generated(hash = 1106755047)
    public ServiceHistory() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
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


    public String getObs() {
        return this.obs;
    }


    public void setObs(String obs) {
        this.obs = obs;
    }



}
