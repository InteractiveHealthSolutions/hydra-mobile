package com.ihsinformatics.dynamicformsgenerator.data.database;

import com.ihsinformatics.dynamicformsgenerator.utils.Logger;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Entity(nameInDb = "form")
public class SaveableForm {
	
	public final static String COLUMN_FORM_TYPE_ID = "form_type_id";
	public final static String COLUMN_FORM_ID = "form_id";
	public final static String ENCOUNTER_TYPE = "encounter_type";
	public final static String COLUMN_FORM_DATA = "data";
	public final static String COLUMN_FORM_ERROR = "form_error";

	public final static String COLUMN_FORM_SERVICE_HISTORY = "form_values";
	public final static String COLUMN_FORM_PATIENT_IDENTITIER= "patient_identifier";
	public final static String COLUMN_FORM_PATIENT_NAME= "patient_name";


	public final static String WORKFLOW_UUID = "workflow_uuid";
	public final static String COMPONENT_FORM_UUID = "component_form_uuid";

	public final static String COLUMN_UPLOAD_ERROR = "last_upload_error";
	public final static String COLUMN_HYDRA_URL = "hydraURL";

	public final static String COLUMN_FILLED_DATE = "filled_date";
	public final static String COLUMN_USERNAME = "filler_username";


	// Added for backward compatibility only, newer calls should use stringFormData
	@Transient
    JSONObject formData;

	@NotNull
    @Property(nameInDb = COLUMN_FORM_TYPE_ID)
	private int formTypeId;
    @Id(autoincrement = true)
	private Long formId;
    @NotNull
    @Property(nameInDb = COLUMN_FORM_DATA)
    private String stringFormData;
    @Property(nameInDb = ENCOUNTER_TYPE)
	private String encounterType;
    @Property(nameInDb = COLUMN_FORM_ERROR)
	private String formError;
	@Property(nameInDb = COLUMN_FORM_SERVICE_HISTORY)
	private String formValues;

	@Property(nameInDb = COLUMN_FORM_PATIENT_IDENTITIER)
	private String identifier;
	@Property(nameInDb = COLUMN_FORM_PATIENT_NAME)
	private String patient_name;

	@Property(nameInDb = WORKFLOW_UUID)
	private String workflowUUID;

	@Property(nameInDb = COMPONENT_FORM_UUID)
	private String componentFormUUID;

	@Property(nameInDb = COLUMN_UPLOAD_ERROR)
	private String lastUploadError;

	@Property(nameInDb = COLUMN_HYDRA_URL)
	private String hydraURL;

	@Property(nameInDb = COLUMN_FILLED_DATE)
	private String filled_date;

	@Property(nameInDb = COLUMN_USERNAME)
	private String filler_username;

	@Generated(hash = 1035108496)
	public SaveableForm(int formTypeId, Long formId, @NotNull String stringFormData,
			String encounterType, String formError, String formValues, String identifier,
			String patient_name, String workflowUUID, String componentFormUUID,
			String lastUploadError, String hydraURL, String filled_date,
			String filler_username) {
		this.formTypeId = formTypeId;
		this.formId = formId;
		this.stringFormData = stringFormData;
		this.encounterType = encounterType;
		this.formError = formError;
		this.formValues = formValues;
		this.identifier = identifier;
		this.patient_name = patient_name;
		this.workflowUUID = workflowUUID;
		this.componentFormUUID = componentFormUUID;
		this.lastUploadError = lastUploadError;
		this.hydraURL = hydraURL;
		this.filled_date = filled_date;
		this.filler_username = filler_username;
	}

	@Generated(hash = 1724246510)
	public SaveableForm() {
	}


	public SaveableForm(int formTypeId, String stringFormData, String formError) {
		super();
		this.formTypeId = formTypeId;
		this.stringFormData = stringFormData;
		this.formError = formError;
	}


	/*public SaveableForm(int formTypeId, int formId, String formData, String formError) {
		super();
		this.formTypeId = formTypeId;
		this.formId = formId;

		try {
			this.formData = new JSONArray(formData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.formError = formError;
	}*/

	public int getFormTypeId() {
		return this.formTypeId;
	}

	public void setFormTypeId(int formTypeId) {
		this.formTypeId = formTypeId;
	}

	public Long getFormId() {
		return this.formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public String[] getExportableData() {
		return new String[]{formTypeId+"", formId+"", stringFormData};
	}

	public String getStringFormData() {
		return this.stringFormData;
	}

	public void setStringFormData(String stringFormData) {
		this.stringFormData = stringFormData;
	}

	public JSONObject getFormData() {
		try {
			formData = new JSONObject(stringFormData);
		} catch (JSONException e) {
			Logger.log(e);
		}

		return formData;
	}

	public void setFormData(JSONObject formData) {
		this.formData = formData;
		stringFormData = formData.toString();
	}



	public String getEncounterType() {
		return this.encounterType;
	}

	public void setEncounterType(String encounterType) {
		this.encounterType = encounterType;
	}

	public String getFormError() {
		return this.formError;
	}

	public void setFormError(String formError) {
		this.formError = formError;
	}

	public String getFormValues() {
		return this.formValues;
	}

	public void setFormValues(String formValues) {
		this.formValues = formValues;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getPatient_name() {
		return this.patient_name;
	}

	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
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

	public String getLastUploadError() {
		return this.lastUploadError;
	}

	public void setLastUploadError(String lastUploadError) {
		this.lastUploadError = lastUploadError;
	}

	public String getHydraURL() {
		return this.hydraURL;
	}

	public void setHydraURL(String hydraURL) {
		this.hydraURL = hydraURL;
	}

	public String getFilled_date() {
		return this.filled_date;
	}

	public void setFilled_date(String filled_date) {
		this.filled_date = filled_date;
	}

	public String getFiller_username() {
		return this.filler_username;
	}

	public void setFiller_username(String filler_username) {
		this.filler_username = filler_username;
	}



}
