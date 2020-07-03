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
 * DAO for table "patient".
*/
public class PatientDao extends AbstractDao<Patient, Long> {

    public static final String TABLENAME = "patient";

    /**
     * Properties of entity Patient.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property FirstName = new Property(1, String.class, "firstName", false, "FIRST_NAME");
        public final static Property LastName = new Property(2, String.class, "lastName", false, "LAST_NAME");
        public final static Property MiddleName = new Property(3, String.class, "middleName", false, "MIDDLE_NAME");
        public final static Property Gender = new Property(4, String.class, "gender", false, "GENDER");
        public final static Property Dob = new Property(5, Long.class, "dob", false, "DOB");
    }


    public PatientDao(DaoConfig config) {
        super(config);
    }
    
    public PatientDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"patient\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"FIRST_NAME\" TEXT," + // 1: firstName
                "\"LAST_NAME\" TEXT," + // 2: lastName
                "\"MIDDLE_NAME\" TEXT," + // 3: middleName
                "\"GENDER\" TEXT," + // 4: gender
                "\"DOB\" INTEGER);"); // 5: dob
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"patient\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Patient entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String firstName = entity.getFirstName();
        if (firstName != null) {
            stmt.bindString(2, firstName);
        }
 
        String lastName = entity.getLastName();
        if (lastName != null) {
            stmt.bindString(3, lastName);
        }
 
        String middleName = entity.getMiddleName();
        if (middleName != null) {
            stmt.bindString(4, middleName);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(5, gender);
        }
 
        Long dob = entity.getDob();
        if (dob != null) {
            stmt.bindLong(6, dob);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Patient entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String firstName = entity.getFirstName();
        if (firstName != null) {
            stmt.bindString(2, firstName);
        }
 
        String lastName = entity.getLastName();
        if (lastName != null) {
            stmt.bindString(3, lastName);
        }
 
        String middleName = entity.getMiddleName();
        if (middleName != null) {
            stmt.bindString(4, middleName);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(5, gender);
        }
 
        Long dob = entity.getDob();
        if (dob != null) {
            stmt.bindLong(6, dob);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public Patient readEntity(Cursor cursor, int offset) {
        Patient entity = new Patient( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // firstName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // lastName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // middleName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // gender
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // dob
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Patient entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setFirstName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLastName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMiddleName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGender(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDob(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Patient entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Patient entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Patient entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
