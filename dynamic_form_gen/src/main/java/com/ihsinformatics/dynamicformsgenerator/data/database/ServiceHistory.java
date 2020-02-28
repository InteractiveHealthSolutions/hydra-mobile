package com.ihsinformatics.dynamicformsgenerator.data.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "history")
public class ServiceHistory {

    public final static String COLUMN_FORM_PATIENT_ID = "patient_id";
    public final static String COLUMN_FORM_ID = "form_id";
    public final static String ENCOUNTER = "encounter";
    public final static String COLUMN_FORM_OBS = "obs";


    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Property(nameInDb = COLUMN_FORM_PATIENT_ID)
    private int patient_id;

    @NotNull
    @Property(nameInDb = COLUMN_FORM_ID)
    private String form_id;

    @NotNull
    @Property(nameInDb = ENCOUNTER)
    private String encounter;


    @Property(nameInDb = COLUMN_FORM_OBS)
    private String obs;


    @Generated(hash = 603241727)
    public ServiceHistory(Long id, int patient_id, @NotNull String form_id,
            @NotNull String encounter, String obs) {
        this.id = id;
        this.patient_id = patient_id;
        this.form_id = form_id;
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


    public int getPatient_id() {
        return this.patient_id;
    }


    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }


    public String getForm_id() {
        return this.form_id;
    }


    public void setForm_id(String form_id) {
        this.form_id = form_id;
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
