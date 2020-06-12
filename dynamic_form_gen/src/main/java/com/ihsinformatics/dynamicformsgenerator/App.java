package com.ihsinformatics.dynamicformsgenerator;

import android.app.Application;

import android.content.Context;
import com.ihsinformatics.dynamicformsgenerator.data.database.DaoMaster;
import com.ihsinformatics.dynamicformsgenerator.data.database.DaoSession;
import com.ihsinformatics.dynamicformsgenerator.data.database.DbOpenHelper;


/**
 * Created by Owais on 1/30/2018.
 */
public class App extends Application {
    private static DaoSession mDaoSession;
    private static final String DATABASE_NAME = "greendao_demo.db";

    @Override
    public void onCreate() {
        super.onCreate();
        mDaoSession = new DaoMaster(new DbOpenHelper(this, DATABASE_NAME).getWritableDb()).newSession();
    }

    public static DaoSession getDaoSession(Context context) {

        if(mDaoSession == null) {
            mDaoSession = new DaoMaster(new DbOpenHelper(context, DATABASE_NAME).getWritableDb()).newSession();
        }
        return mDaoSession;
    }
}
