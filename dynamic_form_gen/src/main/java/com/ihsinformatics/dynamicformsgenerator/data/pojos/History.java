package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "history")
public class History {

    public final static String COLUMN_ID = "id";

    public final static String COLUMN_FORM_PATIENT_ID = "patientIdentifier";

    public final static String COLUMN_COMPONENT_FORM_UUID = "componentFormUUID";
    public final static String COLUMN_COMPONENT_FORM_ID = "componentFormID";

    public final static String COLUMN_PHASE_UUID = "phaseUUID";
    public final static String COLUMN_PHASE_ID = "phaseID";

    public final static String COLUMN_COMPONENT_UUID = "componentUUID";
    public final static String COLUMN_COMPONENT_ID = "componentID";

    public final static String COLUMN_WORKFLOW_UUID = "workflowUUID";
    public final static String COLUMN_WORKFLOW_ID = "WorkflowID";

    public final static String COLUMN_FORM_UUID = "formUUID";
    public final static String COLUMN_FORM_ID = "formID";

    public final static String ENCOUNTER = "encounter";

    public final static String ENCOUNTER_DATE_TIME = "encounterDateTime";
    public final static String ENCOUNTER_SAVED = "encounterSaved";


    @Id(autoincrement = true)
    @Property(nameInDb = COLUMN_ID)
    private Long id;

    @NotNull
    @Property(nameInDb = COLUMN_FORM_PATIENT_ID)
    private String patientIdentifier;

    @Property(nameInDb = COLUMN_COMPONENT_FORM_UUID)
    private String componentFormUUID;
    @Property(nameInDb = COLUMN_COMPONENT_FORM_ID)
    private long componentFormID;

    @Property(nameInDb = COLUMN_PHASE_UUID)
    private String phaseUUID;
    @Property(nameInDb = COLUMN_PHASE_ID)
    private long phaseID;

    @Property(nameInDb = COLUMN_COMPONENT_UUID)
    private String componentUUID;
    @Property(nameInDb = COLUMN_COMPONENT_ID)
    private long componentID;

    @Property(nameInDb = COLUMN_WORKFLOW_UUID)
    private String workflowUUID;
    @Property(nameInDb = COLUMN_WORKFLOW_ID)
    private long WorkflowID;

    @Property(nameInDb = COLUMN_FORM_UUID)
    private String formUUID;
    @Property(nameInDb = COLUMN_FORM_ID)
    private long formID;


    @Property(nameInDb = ENCOUNTER)
    private String encounter;

    @Property(nameInDb = ENCOUNTER_DATE_TIME)
    private String encounterDateTime;

    @Property(nameInDb = ENCOUNTER_SAVED)
    private String encounterSaved;

    @Generated(hash = 600876535)
    public History(Long id, @NotNull String patientIdentifier,
            String componentFormUUID, long componentFormID, String phaseUUID,
            long phaseID, String componentUUID, long componentID,
            String workflowUUID, long WorkflowID, String formUUID, long formID,
            String encounter, String encounterDateTime, String encounterSaved) {
        this.id = id;
        this.patientIdentifier = patientIdentifier;
        this.componentFormUUID = componentFormUUID;
        this.componentFormID = componentFormID;
        this.phaseUUID = phaseUUID;
        this.phaseID = phaseID;
        this.componentUUID = componentUUID;
        this.componentID = componentID;
        this.workflowUUID = workflowUUID;
        this.WorkflowID = WorkflowID;
        this.formUUID = formUUID;
        this.formID = formID;
        this.encounter = encounter;
        this.encounterDateTime = encounterDateTime;
        this.encounterSaved = encounterSaved;
    }

    @Generated(hash = 869423138)
    public History() {
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

    public String getComponentFormUUID() {
        return this.componentFormUUID;
    }

    public void setComponentFormUUID(String componentFormUUID) {
        this.componentFormUUID = componentFormUUID;
    }

    public long getComponentFormID() {
        return this.componentFormID;
    }

    public void setComponentFormID(long componentFormID) {
        this.componentFormID = componentFormID;
    }

    public String getPhaseUUID() {
        return this.phaseUUID;
    }

    public void setPhaseUUID(String phaseUUID) {
        this.phaseUUID = phaseUUID;
    }

    public long getPhaseID() {
        return this.phaseID;
    }

    public void setPhaseID(long phaseID) {
        this.phaseID = phaseID;
    }

    public String getComponentUUID() {
        return this.componentUUID;
    }

    public void setComponentUUID(String componentUUID) {
        this.componentUUID = componentUUID;
    }

    public long getComponentID() {
        return this.componentID;
    }

    public void setComponentID(long componentID) {
        this.componentID = componentID;
    }

    public String getWorkflowUUID() {
        return this.workflowUUID;
    }

    public void setWorkflowUUID(String workflowUUID) {
        this.workflowUUID = workflowUUID;
    }

    public long getWorkflowID() {
        return this.WorkflowID;
    }

    public void setWorkflowID(long WorkflowID) {
        this.WorkflowID = WorkflowID;
    }

    public String getFormUUID() {
        return this.formUUID;
    }

    public void setFormUUID(String formUUID) {
        this.formUUID = formUUID;
    }

    public long getFormID() {
        return this.formID;
    }

    public void setFormID(long formID) {
        this.formID = formID;
    }

    public String getEncounter() {
        return this.encounter;
    }

    public void setEncounter(String encounter) {
        this.encounter = encounter;
    }

    public String getEncounterDateTime() {
        return this.encounterDateTime;
    }

    public void setEncounterDateTime(String encounterDateTime) {
        this.encounterDateTime = encounterDateTime;
    }

    public String getEncounterSaved() {
        return this.encounterSaved;
    }

    public void setEncounterSaved(String encounterSaved) {
        this.encounterSaved = encounterSaved;
    }
}
