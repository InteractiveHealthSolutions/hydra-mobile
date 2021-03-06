package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "form".
*/
public class SaveableFormDao extends AbstractDao<SaveableForm, Long> {

    public static final String TABLENAME = "form";

    /**
     * Properties of entity SaveableForm.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property FormTypeId = new Property(0, int.class, "formTypeId", false, "form_type_id");
        public final static Property FormId = new Property(1, Long.class, "formId", true, "_id");
        public final static Property StringFormData = new Property(2, String.class, "stringFormData", false, "data");
        public final static Property EncounterType = new Property(3, String.class, "encounterType", false, "encounter_type");
        public final static Property FormError = new Property(4, String.class, "formError", false, "form_error");
        public final static Property FormValues = new Property(5, String.class, "formValues", false, "form_values");
        public final static Property Identifier = new Property(6, String.class, "identifier", false, "patient_identifier");
        public final static Property Patient_name = new Property(7, String.class, "patient_name", false, "patient_name");
        public final static Property WorkflowUUID = new Property(8, String.class, "workflowUUID", false, "workflow_uuid");
        public final static Property ComponentFormUUID = new Property(9, String.class, "componentFormUUID", false, "component_form_uuid");
        public final static Property LastUploadError = new Property(10, String.class, "lastUploadError", false, "last_upload_error");
        public final static Property HydraURL = new Property(11, String.class, "hydraURL", false, "hydraURL");
    }


    public SaveableFormDao(DaoConfig config) {
        super(config);
    }
    
    public SaveableFormDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"form\" (" + //
                "\"form_type_id\" INTEGER NOT NULL ," + // 0: formTypeId
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 1: formId
                "\"data\" TEXT NOT NULL ," + // 2: stringFormData
                "\"encounter_type\" TEXT," + // 3: encounterType
                "\"form_error\" TEXT," + // 4: formError
                "\"form_values\" TEXT," + // 5: formValues
                "\"patient_identifier\" TEXT," + // 6: identifier
                "\"patient_name\" TEXT," + // 7: patient_name
                "\"workflow_uuid\" TEXT," + // 8: workflowUUID
                "\"component_form_uuid\" TEXT," + // 9: componentFormUUID
                "\"last_upload_error\" TEXT," + // 10: lastUploadError
                "\"hydraURL\" TEXT);"); // 11: hydraURL
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"form\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SaveableForm entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getFormTypeId());
 
        Long formId = entity.getFormId();
        if (formId != null) {
            stmt.bindLong(2, formId);
        }
        stmt.bindString(3, entity.getStringFormData());
 
        String encounterType = entity.getEncounterType();
        if (encounterType != null) {
            stmt.bindString(4, encounterType);
        }
 
        String formError = entity.getFormError();
        if (formError != null) {
            stmt.bindString(5, formError);
        }
 
        String formValues = entity.getFormValues();
        if (formValues != null) {
            stmt.bindString(6, formValues);
        }
 
        String identifier = entity.getIdentifier();
        if (identifier != null) {
            stmt.bindString(7, identifier);
        }
 
        String patient_name = entity.getPatient_name();
        if (patient_name != null) {
            stmt.bindString(8, patient_name);
        }
 
        String workflowUUID = entity.getWorkflowUUID();
        if (workflowUUID != null) {
            stmt.bindString(9, workflowUUID);
        }
 
        String componentFormUUID = entity.getComponentFormUUID();
        if (componentFormUUID != null) {
            stmt.bindString(10, componentFormUUID);
        }
 
        String lastUploadError = entity.getLastUploadError();
        if (lastUploadError != null) {
            stmt.bindString(11, lastUploadError);
        }
 
        String hydraURL = entity.getHydraURL();
        if (hydraURL != null) {
            stmt.bindString(12, hydraURL);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SaveableForm entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getFormTypeId());
 
        Long formId = entity.getFormId();
        if (formId != null) {
            stmt.bindLong(2, formId);
        }
        stmt.bindString(3, entity.getStringFormData());
 
        String encounterType = entity.getEncounterType();
        if (encounterType != null) {
            stmt.bindString(4, encounterType);
        }
 
        String formError = entity.getFormError();
        if (formError != null) {
            stmt.bindString(5, formError);
        }
 
        String formValues = entity.getFormValues();
        if (formValues != null) {
            stmt.bindString(6, formValues);
        }
 
        String identifier = entity.getIdentifier();
        if (identifier != null) {
            stmt.bindString(7, identifier);
        }
 
        String patient_name = entity.getPatient_name();
        if (patient_name != null) {
            stmt.bindString(8, patient_name);
        }
 
        String workflowUUID = entity.getWorkflowUUID();
        if (workflowUUID != null) {
            stmt.bindString(9, workflowUUID);
        }
 
        String componentFormUUID = entity.getComponentFormUUID();
        if (componentFormUUID != null) {
            stmt.bindString(10, componentFormUUID);
        }
 
        String lastUploadError = entity.getLastUploadError();
        if (lastUploadError != null) {
            stmt.bindString(11, lastUploadError);
        }
 
        String hydraURL = entity.getHydraURL();
        if (hydraURL != null) {
            stmt.bindString(12, hydraURL);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1);
    }    

    @Override
    public SaveableForm readEntity(Cursor cursor, int offset) {
        SaveableForm entity = new SaveableForm( //
            cursor.getInt(offset + 0), // formTypeId
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // formId
            cursor.getString(offset + 2), // stringFormData
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // encounterType
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // formError
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // formValues
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // identifier
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // patient_name
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // workflowUUID
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // componentFormUUID
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // lastUploadError
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // hydraURL
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SaveableForm entity, int offset) {
        entity.setFormTypeId(cursor.getInt(offset + 0));
        entity.setFormId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setStringFormData(cursor.getString(offset + 2));
        entity.setEncounterType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFormError(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFormValues(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIdentifier(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPatient_name(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setWorkflowUUID(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setComponentFormUUID(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setLastUploadError(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setHydraURL(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SaveableForm entity, long rowId) {
        entity.setFormId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SaveableForm entity) {
        if(entity != null) {
            return entity.getFormId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SaveableForm entity) {
        return entity.getFormId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
