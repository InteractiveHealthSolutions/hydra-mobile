package com.ihsinformatics.dynamicformsgenerator.data.database;

import com.ihsinformatics.dynamicformsgenerator.network.pojos.Patient;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.json.JSONObject;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "offline_patient")
public class OfflinePatient {

	public final static String COLUMN_MR_NUMBER = "mr_number";
	public final static String COLUMN_PQ_IDENTIFIER = "pq_id";
	public final static String COLUMN_SC_IDENTIFIER = "sc_id";
	public final static String COLUMN_PATIENT_NAME = "patient_name";
	public final static String COLUMN_GENDER = "gender";
	public final static String COLUMN_DOB = "date_ofBirth";
	public final static String COLUMN_ENCOUNTERS_JSON = "encounters_json";
	public final static String COLUMN_FIELDS_DATA_JSON = "fields_data_json";

	// Added for backward compatibility only, newer calls should use stringFormData
	@Transient
	JSONObject formData;

	@NotNull
	@Unique
	@Property(nameInDb = COLUMN_MR_NUMBER)
	private String mrNumber;

	@Unique
	@Property(nameInDb = COLUMN_PQ_IDENTIFIER)
	private String pqId;

	@Unique
	@Property(nameInDb = COLUMN_SC_IDENTIFIER)
	private String scId;

	@Id(autoincrement = true)
	private Long id;

	@Property(nameInDb = COLUMN_PATIENT_NAME)
	private String name;

	@NotNull
	@Property(nameInDb = COLUMN_GENDER)
	private String gender;

	@Property(nameInDb = COLUMN_DOB)
	private Long dob;

	@NotNull
	@Property(nameInDb = COLUMN_ENCOUNTERS_JSON)
	private String encounterJson;

	@Property(nameInDb = COLUMN_FIELDS_DATA_JSON)
	private String fieldDataJson;


	public OfflinePatient(Patient patient) {
		this.gender = patient.getGender();
		this.dob = patient.getBirthDate().getTime();
	}
	@Generated(hash = 572999873)
	public OfflinePatient(@NotNull String mrNumber, String pqId, String scId,
									Long id, String name, @NotNull String gender, Long dob,
									@NotNull String encounterJson, String fieldDataJson) {
					this.mrNumber = mrNumber;
					this.pqId = pqId;
					this.scId = scId;
					this.id = id;
					this.name = name;
					this.gender = gender;
					this.dob = dob;
					this.encounterJson = encounterJson;
					this.fieldDataJson = fieldDataJson;
	}
	@Generated(hash = 495313978)
	public OfflinePatient() {
	}
	public String getMrNumber() {
					return this.mrNumber;
	}
	public void setMrNumber(String mrNumber) {
					this.mrNumber = mrNumber;
	}
	public Long getId() {
					return this.id;
	}
	public void setId(Long id) {
					this.id = id;
	}
	public String getGender() {
					return this.gender;
	}
	public void setGender(String gender) {
					this.gender = gender;
	}
	public Long getDob() {
					return this.dob;
	}
	public void setDob(Long dob) {
					this.dob = dob;
	}
	public String getEncounterJson() {
		return this.encounterJson;
	}
	public void setEncounterJson(String encounterJson) {
		this.encounterJson = encounterJson;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFieldDataJson() {
		return this.fieldDataJson;
	}
	public void setFieldDataJson(String fieldDataJson) {
		this.fieldDataJson = fieldDataJson;
	}
	public String getPqId() {
					return this.pqId;
	}
	public void setPqId(String pqId) {
					this.pqId = pqId;
	}
	public String getScId() {
					return this.scId;
	}
	public void setScId(String scId) {
					this.scId = scId;
	}
}