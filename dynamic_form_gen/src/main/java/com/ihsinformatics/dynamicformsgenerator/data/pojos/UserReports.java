package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "UserReports")
public class UserReports {

    public final static String COLUMN_USERNAME = "username";
    public final static String ENCOUNTER = "encounter";
    public final static String ENCOUNTER_DATE = "date";
    public final static String ENCOUNTER_UPLOADED = "encounter_uploaded";
    public final static String OFFLINE_FORM_ID = "offline_form_id";

    public final static String ENCOUNTER_WORKFLOWUUID = "workflowUUID";
    public final static String ENCOUNTER_COMPONENT_FORM_UUID = "componentFormUUID";
    public final static String COLUMN_URL = "url";



    @Property(nameInDb = COLUMN_USERNAME)
    private String username;

    @Property(nameInDb = ENCOUNTER)
    private String encounter;

    @Property(nameInDb = OFFLINE_FORM_ID)
    private long offline_form_id;

    @Property(nameInDb = ENCOUNTER_UPLOADED)
    private int encounter_uploaded;

    @Property(nameInDb = ENCOUNTER_DATE)
    private String date;

    @Property(nameInDb = ENCOUNTER_WORKFLOWUUID)
    private String workflowUUID;

    @Property(nameInDb = ENCOUNTER_COMPONENT_FORM_UUID)
    private String componentFormUUID;

    @Property(nameInDb = COLUMN_URL)
    private String url;

    @Id(autoincrement = true)
    private Long id;

    @Generated(hash = 1477670716)
    public UserReports(String username, String encounter, long offline_form_id,
            int encounter_uploaded, String date, String workflowUUID,
            String componentFormUUID, String url, Long id) {
        this.username = username;
        this.encounter = encounter;
        this.offline_form_id = offline_form_id;
        this.encounter_uploaded = encounter_uploaded;
        this.date = date;
        this.workflowUUID = workflowUUID;
        this.componentFormUUID = componentFormUUID;
        this.url = url;
        this.id = id;
    }

    @Generated(hash = 2030018550)
    public UserReports() {
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncounter() {
        return this.encounter;
    }

    public void setEncounter(String encounter) {
        this.encounter = encounter;
    }

    public long getOffline_form_id() {
        return this.offline_form_id;
    }

    public void setOffline_form_id(long offline_form_id) {
        this.offline_form_id = offline_form_id;
    }

    public int getEncounter_uploaded() {
        return this.encounter_uploaded;
    }

    public void setEncounter_uploaded(int encounter_uploaded) {
        this.encounter_uploaded = encounter_uploaded;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWorkflowUUID() {
        return this.workflowUUID;
    }

    public void setWorkflowUUID(String workflowUUID) {
        this.workflowUUID = workflowUUID;
    }

    public String getComponentFormUUID() {
        return this.componentFormUUID;
    }

    public void setComponentFormUUID(String componentFormUUID) {
        this.componentFormUUID = componentFormUUID;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}