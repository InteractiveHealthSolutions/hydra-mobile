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

	public SaveableForm() {
		
	}



	public SaveableForm(int formTypeId, String stringFormData, String formError) {
		super();
		this.formTypeId = formTypeId;
		this.stringFormData = stringFormData;
		this.formError = formError;
	}



	@Generated(hash = 106453358)
	public SaveableForm(int formTypeId, Long formId, @NotNull String stringFormData,
			String encounterType, String formError, String formValues, String identifier) {
		this.formTypeId = formTypeId;
		this.formId = formId;
		this.stringFormData = stringFormData;
		this.encounterType = encounterType;
		this.formError = formError;
		this.formValues = formValues;
		this.identifier = identifier;
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
		return formTypeId;
	}

	public void setFormTypeId(int formTypeId) {
		this.formTypeId = formTypeId;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}
	
	public String[] getExportableData() {
		return new String[]{formTypeId+"", formId+"", stringFormData};
	}

	public String getFormError(){return formError;}

	public void setFormError(String formError) {this.formError = formError;}

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
}
