package com.ihsinformatics.dynamicformsgenerator.data.database;

import android.content.Context;
import android.util.Log;

import com.ihsinformatics.dynamicformsgenerator.data.pojos.User;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserDao;

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
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                try {
                    db.execSQL("ALTER TABLE " + OfflinePatientDao.TABLENAME + " ADD COLUMN " + OfflinePatientDao.Properties.CovidResult.columnName + " TEXT DEFAULT 'NONE'");
                    db.execSQL("ALTER TABLE " + SaveableFormDao.TABLENAME + " ADD COLUMN " + SaveableFormDao.Properties.LastUploadError.columnName);
                    db.execSQL("ALTER TABLE " + SaveableFormDao.TABLENAME + " ADD COLUMN " + SaveableFormDao.Properties.Patient_name.columnName);
                    db.execSQL("ALTER TABLE " + SaveableFormDao.TABLENAME + " ADD COLUMN " + SaveableFormDao.Properties.HydraURL.columnName);
                }catch (Exception e){

                }
            case 17:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + UserDao.TABLENAME + " (" + User.COLUMN_USERUUID + " TEXT, "
                        +User.COLUMN_USERNAME + " TEXT, "
                        +User.COLUMN_PASSWORD + " TEXT, "
                        +User.COLUMN_PROVIDER + " TEXT, "
                        +"ID INTEGER PRIMARY KEY AUTOINCREMENT"+" )");
        }
    }
}
