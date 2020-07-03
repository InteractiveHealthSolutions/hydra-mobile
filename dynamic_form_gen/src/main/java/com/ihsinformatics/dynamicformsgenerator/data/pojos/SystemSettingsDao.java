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
 * DAO for table "SYSTEM_SETTINGS".
*/
public class SystemSettingsDao extends AbstractDao<SystemSettings, Long> {

    public static final String TABLENAME = "SYSTEM_SETTINGS";

    /**
     * Properties of entity SystemSettings.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Property = new Property(1, String.class, "property", false, "PROPERTY");
        public final static Property Value = new Property(2, String.class, "value", false, "VALUE");
        public final static Property Uuid = new Property(3, String.class, "uuid", false, "UUID");
    }


    public SystemSettingsDao(DaoConfig config) {
        super(config);
    }
    
    public SystemSettingsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SYSTEM_SETTINGS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PROPERTY\" TEXT," + // 1: property
                "\"VALUE\" TEXT," + // 2: value
                "\"UUID\" TEXT UNIQUE );"); // 3: uuid
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SYSTEM_SETTINGS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SystemSettings entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String property = entity.getProperty();
        if (property != null) {
            stmt.bindString(2, property);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(3, value);
        }
 
        String uuid = entity.getUuid();
        if (uuid != null) {
            stmt.bindString(4, uuid);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SystemSettings entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String property = entity.getProperty();
        if (property != null) {
            stmt.bindString(2, property);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(3, value);
        }
 
        String uuid = entity.getUuid();
        if (uuid != null) {
            stmt.bindString(4, uuid);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SystemSettings readEntity(Cursor cursor, int offset) {
        SystemSettings entity = new SystemSettings( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // property
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // value
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // uuid
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SystemSettings entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setProperty(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setValue(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUuid(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SystemSettings entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SystemSettings entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SystemSettings entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
