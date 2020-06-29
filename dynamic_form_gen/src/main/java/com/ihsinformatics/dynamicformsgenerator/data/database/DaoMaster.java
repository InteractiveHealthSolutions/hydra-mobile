package com.ihsinformatics.dynamicformsgenerator.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

import com.ihsinformatics.dynamicformsgenerator.data.pojos.EncountersWithObsDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormTypeDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.IdentifierTypeDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ImageDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationAttributeDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationAttributeTypeDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationTagDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationTagMapDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.OptionDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.PatientDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.PatientIdentifierDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ProcedureDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.SystemSettingsDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserCredentialsDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserReportsDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * Master of DAO (schema version 18): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 18;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(Database db, boolean ifNotExists) {
        OfflinePatientDao.createTable(db, ifNotExists);
        SaveableFormDao.createTable(db, ifNotExists);
        ServiceHistoryDao.createTable(db, ifNotExists);
        EncountersWithObsDao.createTable(db, ifNotExists);
        FormTypeDao.createTable(db, ifNotExists);
        IdentifierTypeDao.createTable(db, ifNotExists);
        ImageDao.createTable(db, ifNotExists);
        LocationDao.createTable(db, ifNotExists);
        LocationAttributeDao.createTable(db, ifNotExists);
        LocationAttributeTypeDao.createTable(db, ifNotExists);
        LocationTagDao.createTable(db, ifNotExists);
        LocationTagMapDao.createTable(db, ifNotExists);
        OptionDao.createTable(db, ifNotExists);
        PatientDao.createTable(db, ifNotExists);
        PatientIdentifierDao.createTable(db, ifNotExists);
        ProcedureDao.createTable(db, ifNotExists);
        SystemSettingsDao.createTable(db, ifNotExists);
        UserDao.createTable(db, ifNotExists);
        UserCredentialsDao.createTable(db, ifNotExists);
        UserReportsDao.createTable(db, ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(Database db, boolean ifExists) {
        OfflinePatientDao.dropTable(db, ifExists);
        SaveableFormDao.dropTable(db, ifExists);
        ServiceHistoryDao.dropTable(db, ifExists);
        EncountersWithObsDao.dropTable(db, ifExists);
        FormTypeDao.dropTable(db, ifExists);
        IdentifierTypeDao.dropTable(db, ifExists);
        ImageDao.dropTable(db, ifExists);
        LocationDao.dropTable(db, ifExists);
        LocationAttributeDao.dropTable(db, ifExists);
        LocationAttributeTypeDao.dropTable(db, ifExists);
        LocationTagDao.dropTable(db, ifExists);
        LocationTagMapDao.dropTable(db, ifExists);
        OptionDao.dropTable(db, ifExists);
        PatientDao.dropTable(db, ifExists);
        PatientIdentifierDao.dropTable(db, ifExists);
        ProcedureDao.dropTable(db, ifExists);
        SystemSettingsDao.dropTable(db, ifExists);
        UserDao.dropTable(db, ifExists);
        UserCredentialsDao.dropTable(db, ifExists);
        UserReportsDao.dropTable(db, ifExists);
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     * Convenience method using a {@link DevOpenHelper}.
     */
    public static DaoSession newDevSession(Context context, String name) {
        Database db = new DevOpenHelper(context, name).getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(OfflinePatientDao.class);
        registerDaoClass(SaveableFormDao.class);
        registerDaoClass(ServiceHistoryDao.class);
        registerDaoClass(EncountersWithObsDao.class);
        registerDaoClass(FormTypeDao.class);
        registerDaoClass(IdentifierTypeDao.class);
        registerDaoClass(ImageDao.class);
        registerDaoClass(LocationDao.class);
        registerDaoClass(LocationAttributeDao.class);
        registerDaoClass(LocationAttributeTypeDao.class);
        registerDaoClass(LocationTagDao.class);
        registerDaoClass(LocationTagMapDao.class);
        registerDaoClass(OptionDao.class);
        registerDaoClass(PatientDao.class);
        registerDaoClass(PatientIdentifierDao.class);
        registerDaoClass(ProcedureDao.class);
        registerDaoClass(SystemSettingsDao.class);
        registerDaoClass(UserDao.class);
        registerDaoClass(UserCredentialsDao.class);
        registerDaoClass(UserReportsDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    /**
     * Calls {@link #createAllTables(Database, boolean)} in {@link #onCreate(Database)} -
     */
    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

}
