package com.ihsinformatics.dynamicformsgenerator.data.database;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Owais on 1/30/2018.
 */
public class DbOpenHelper extends DaoMaster.OpenHelper {

    public DbOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d("DEBUG", "DB_OLD_VERSION : " + oldVersion + ", DB_NEW_VERSION : " + newVersion);
        switch (oldVersion) {
            case 1:
            case 2:
                //db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " ADD COLUMN " + UserDao.Properties.Name.columnName + " TEXT DEFAULT 'DEFAULT_VAL'");
            case 15:
                db.execSQL("ALTER TABLE " + OfflinePatientDao.TABLENAME + " ADD COLUMN " + OfflinePatientDao.Properties.CovidResult.columnName + " TEXT DEFAULT 'NONE'");
                db.execSQL("ALTER TABLE " + SaveableFormDao.TABLENAME + " ADD COLUMN " + SaveableFormDao.Properties.LastUploadError.columnName);
                db.execSQL("ALTER TABLE " + SaveableFormDao.TABLENAME + " ADD COLUMN " + SaveableFormDao.Properties.Patient_name.columnName);
                db.execSQL("ALTER TABLE " + SaveableFormDao.TABLENAME + " ADD COLUMN " + SaveableFormDao.Properties.HydraURL.columnName);


        }
    }
}
