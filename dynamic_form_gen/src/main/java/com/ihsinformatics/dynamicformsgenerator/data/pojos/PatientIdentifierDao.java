package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PATIENT_IDENTIFIER".
*/
public class PatientIdentifierDao extends AbstractDao<PatientIdentifier, Long> {

    public static final String TABLENAME = "PATIENT_IDENTIFIER";

    /**
     * Properties of entity PatientIdentifier.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property IdentifierTypeId = new Property(1, Long.class, "identifierTypeId", false, "IDENTIFIER_TYPE_ID");
        public final static Property PatientId = new Property(2, Long.class, "patientId", false, "PATIENT_ID");
    }

    private DaoSession daoSession;


    public PatientIdentifierDao(DaoConfig config) {
        super(config);
    }
    
    public PatientIdentifierDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PATIENT_IDENTIFIER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"IDENTIFIER_TYPE_ID\" INTEGER," + // 1: identifierTypeId
                "\"PATIENT_ID\" INTEGER);"); // 2: patientId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PATIENT_IDENTIFIER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PatientIdentifier entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        Long identifierTypeId = entity.getIdentifierTypeId();
        if (identifierTypeId != null) {
            stmt.bindLong(2, identifierTypeId);
        }
 
        Long patientId = entity.getPatientId();
        if (patientId != null) {
            stmt.bindLong(3, patientId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PatientIdentifier entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        Long identifierTypeId = entity.getIdentifierTypeId();
        if (identifierTypeId != null) {
            stmt.bindLong(2, identifierTypeId);
        }
 
        Long patientId = entity.getPatientId();
        if (patientId != null) {
            stmt.bindLong(3, patientId);
        }
    }

    @Override
    protected final void attachEntity(PatientIdentifier entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public PatientIdentifier readEntity(Cursor cursor, int offset) {
        PatientIdentifier entity = new PatientIdentifier( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // identifierTypeId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2) // patientId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PatientIdentifier entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setIdentifierTypeId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setPatientId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PatientIdentifier entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PatientIdentifier entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PatientIdentifier entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getIdentifierTypeDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getPatientDao().getAllColumns());
            builder.append(" FROM PATIENT_IDENTIFIER T");
            builder.append(" LEFT JOIN IDENTIFIER_TYPE T0 ON T.\"IDENTIFIER_TYPE_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN patient T1 ON T.\"PATIENT_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected PatientIdentifier loadCurrentDeep(Cursor cursor, boolean lock) {
        PatientIdentifier entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        IdentifierType identifierType = loadCurrentOther(daoSession.getIdentifierTypeDao(), cursor, offset);
        entity.setIdentifierType(identifierType);
        offset += daoSession.getIdentifierTypeDao().getAllColumns().length;

        Patient patient = loadCurrentOther(daoSession.getPatientDao(), cursor, offset);
        entity.setPatient(patient);

        return entity;    
    }

    public PatientIdentifier loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<PatientIdentifier> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<PatientIdentifier> list = new ArrayList<PatientIdentifier>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<PatientIdentifier> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<PatientIdentifier> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
