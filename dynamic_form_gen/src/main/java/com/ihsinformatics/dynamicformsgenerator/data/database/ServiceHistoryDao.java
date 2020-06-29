package com.ihsinformatics.dynamicformsgenerator.data.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "service_history".
*/
public class ServiceHistoryDao extends AbstractDao<ServiceHistory, String> {

    public static final String TABLENAME = "service_history";

    /**
     * Properties of entity ServiceHistory.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property PatientIdentifier = new Property(0, String.class, "patientIdentifier", true, "patientIdentifier");
        public final static Property Encounter = new Property(1, String.class, "encounter", false, "encounter");
    }


    public ServiceHistoryDao(DaoConfig config) {
        super(config);
    }
    
    public ServiceHistoryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"service_history\" (" + //
                "\"patientIdentifier\" TEXT PRIMARY KEY NOT NULL ," + // 0: patientIdentifier
                "\"encounter\" TEXT NOT NULL );"); // 1: encounter
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"service_history\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ServiceHistory entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getPatientIdentifier());
        stmt.bindString(2, entity.getEncounter());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ServiceHistory entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getPatientIdentifier());
        stmt.bindString(2, entity.getEncounter());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    @Override
    public ServiceHistory readEntity(Cursor cursor, int offset) {
        ServiceHistory entity = new ServiceHistory( //
            cursor.getString(offset + 0), // patientIdentifier
            cursor.getString(offset + 1) // encounter
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ServiceHistory entity, int offset) {
        entity.setPatientIdentifier(cursor.getString(offset + 0));
        entity.setEncounter(cursor.getString(offset + 1));
     }
    
    @Override
    protected final String updateKeyAfterInsert(ServiceHistory entity, long rowId) {
        return entity.getPatientIdentifier();
    }
    
    @Override
    public String getKey(ServiceHistory entity) {
        if(entity != null) {
            return entity.getPatientIdentifier();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ServiceHistory entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
