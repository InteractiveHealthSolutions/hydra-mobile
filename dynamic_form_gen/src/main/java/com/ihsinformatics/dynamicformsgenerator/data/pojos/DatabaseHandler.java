package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static DatabaseHandler databaseHandler;
	Context context;
	
	// Database Version
    private static final int DATABASE_VERSION = 7;
 
    // Database Name
    private static final String DATABASE_NAME = "dynamicformsgenerator";
 
    // Table names
    public static final String TABLE_FORMS_TYPE = "form_type";
    public static final String TABLE_FORM = "form";
	public static final String TABLE_IMAGE = "image";

	//!!!
	public static final String TABLE_USER_CREDENTIALS = "user_credentials";

	// Constructor
    private DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
    
    public static DatabaseHandler getInstance(Context context) {
    	if(databaseHandler == null) {
    		databaseHandler = new DatabaseHandler(context);
    	}
    	
    	return databaseHandler;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		String t1 = "CREATE TABLE IF NOT EXISTS "+DatabaseHandler.TABLE_FORMS_TYPE+" ("
				
				+FormType.COLUMN_TYPE_ID + " INTEGER PRIMARY KEY NOT NULL,"
				+FormType.COLUMN_TYPE_NAME + " TEXT NOT NULL)";
		
		String t2 = "CREATE TABLE IF NOT EXISTS "+DatabaseHandler.TABLE_FORM+" ("
	            
				+SaveableForm.COLUMN_FORM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+SaveableForm.COLUMN_FORM_TYPE_ID + " INTEGER NOT NULL,"
	            +SaveableForm.COLUMN_FORM_DATA + " TEXT NOT NULL,"
				+SaveableForm.COLUMN_FORM_ERROR + " TEXT NULL,"
	            + " FOREIGN KEY ("+SaveableForm.COLUMN_FORM_TYPE_ID+") REFERENCES "+DatabaseHandler.TABLE_FORMS_TYPE+" ("+FormType.COLUMN_TYPE_ID+"));";
		//!!!
		String t3 = "CREATE TABLE IF NOT EXISTS "+DatabaseHandler.TABLE_USER_CREDENTIALS+" ("

				+ UserCredentials.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ UserCredentials.COLUMN_USER_NAME + " TEXT NOT NULL,"
				+ UserCredentials.COLUMN_USER_PASSWORD + " TEXT NOT NULL,"
				+ UserCredentials.COLUMN_FULL_NAME+ " TEXT NULL,"
				+ UserCredentials.COLUMN_UUID + " TEXT NOT NULL,"
				+ UserCredentials.COLUMN_PROVIDER_UUID + " TEXT NULL,"
				+ UserCredentials.COLUMN_GENDER + " TEXT NULL)";

        String t4 = "CREATE TABLE IF NOT EXISTS "+DatabaseHandler.TABLE_IMAGE+" ("

                + Image.COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Image.COLUMN_FORM_ID + " INTEGER NOT NULL,"
                + Image.COLUMN_FORM_UUID + " TEXT NULL,"
				+ Image.COLUMN_IMAGE_METADATA + " TEXT NULL,"
                + Image.COLUMN_IMAGE_CONTENT + " BLOB NOT NULL,"
				+ Image.COLUMN_IMAGE_ERROR + " TEXT NULL)";

		db.execSQL(t1);
		db.execSQL(t2);
		db.execSQL(t3);
        db.execSQL(t4);
	//	new DefaultData().insertDefaultData(context, db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*String t1 = "drop table if exists "+DatabaseHandler.TABLE_FORMS_TYPE;
		String t2 = "drop table if exists "+DatabaseHandler.TABLE_FORM;
		
		db.execSQL(t1);
		db.execSQL(t2);*/
		if(oldVersion == 3 && newVersion == 4) {
			String u1 = "ALTER TABLE "+DatabaseHandler.TABLE_FORM+" ADD "+SaveableForm.COLUMN_FORM_ERROR+" TEXT NULL";
			db.execSQL(u1);
		}
		if(oldVersion == 4 && newVersion == 5) {
			onCreate(db);
		}
		if(oldVersion == 5) {
			String u1 = "ALTER TABLE "+DatabaseHandler.TABLE_USER_CREDENTIALS+" ADD " + UserCredentials.COLUMN_FULL_NAME+ " TEXT NULL";
			String u2 = "ALTER TABLE "+DatabaseHandler.TABLE_USER_CREDENTIALS+" ADD " + UserCredentials.COLUMN_GENDER + " TEXT NULL";
			db.execSQL(u1);
			db.execSQL(u2);
		}
		if(oldVersion == 6) {
			// DO nothing
		}

		//new DefaultData().insertDefaultData(context, db, newVersion);
	}
}
