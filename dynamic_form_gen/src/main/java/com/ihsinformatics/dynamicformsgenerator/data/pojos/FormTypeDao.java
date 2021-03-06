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
 * DAO for table "form_type".
*/
public class FormTypeDao extends AbstractDao<FormType, Long> {

    public static final String TABLENAME = "form_type";

    /**
     * Properties of entity FormType.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property TypeId = new Property(0, long.class, "typeId", true, "_id");
        public final static Property TypeName = new Property(1, String.class, "typeName", false, "type_name");
    }


    public FormTypeDao(DaoConfig config) {
        super(config);
    }
    
    public FormTypeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"form_type\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: typeId
                "\"type_name\" TEXT NOT NULL );"); // 1: typeName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"form_type\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FormType entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getTypeId());
        stmt.bindString(2, entity.getTypeName());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FormType entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getTypeId());
        stmt.bindString(2, entity.getTypeName());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public FormType readEntity(Cursor cursor, int offset) {
        FormType entity = new FormType( //
            cursor.getLong(offset + 0), // typeId
            cursor.getString(offset + 1) // typeName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, FormType entity, int offset) {
        entity.setTypeId(cursor.getLong(offset + 0));
        entity.setTypeName(cursor.getString(offset + 1));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(FormType entity, long rowId) {
        entity.setTypeId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(FormType entity) {
        if(entity != null) {
            return entity.getTypeId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(FormType entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
