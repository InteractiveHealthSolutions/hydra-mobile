package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "form_type")
public class FormType {

	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_TYPE_NAME = "type_name";

    @Id
	private long typeId;
    @NotNull
    @Property(nameInDb = COLUMN_TYPE_NAME)
	private String typeName;
	
	public FormType() {
		// TODO Auto-generated constructor stub
	}

	@Generated(hash = 209332077)
	public FormType(long typeId, @NotNull String typeName) {
					this.typeId = typeId;
					this.typeName = typeName;
	}

	

	

	/*public FormType(int typeId, String typeName) {
		super();
		this.typeId = typeId;
		this.typeName = typeName;
	}*/

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public static final String ENCOUNTER_TYPE_REGISTRATION = "REGISTRATION";              
	public static final String ENCOUNTER_TYPE_SCREENING = "SCREENING";                    
	public static final String ENCOUNTER_TYPE_FIRST_FOLLOWUP = "FIRST FOLLOWUP";          
	public static final String ENCOUNTER_TYPE_FOLLOWUP = "FOLLOWUP";                      
	public static final String ENCOUNTER_TYPE_INSTANT_TERMINATION = "INSTANT TERMINATION";
	public static final String ENCOUNTER_TYPE_TERMINATION = "TERMINATION";                
	
}
