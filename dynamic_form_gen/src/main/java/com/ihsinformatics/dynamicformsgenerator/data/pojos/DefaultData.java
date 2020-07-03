package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;

import java.util.List;

public class DefaultData {

	public void insertDefaultData(Context context, SQLiteDatabase db) {
		try {
			
			List<FormType> formTypes = DataProvider.getInstance(context).getFormTypes();

			DataAccess.getInstance().insertFormTypes(context, formTypes, db);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
