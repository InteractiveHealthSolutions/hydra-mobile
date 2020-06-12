package com.ihsinformatics.dynamicformsgenerator.utils;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.SecretKey;

public class Logger {

    private static SimpleDateFormat logDateFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
	public static void log(Exception e) {
		try {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String toWrite = sw.toString();
			File externalStorageDir = Environment.getExternalStorageDirectory();
			File myFile = new File(externalStorageDir, "hydra.txt");

			if (!myFile.exists()) {
				myFile.createNewFile();
			} 
			
			FileOutputStream fOut = new FileOutputStream(myFile, true);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append("\n====================================================================================\n");
			myOutWriter.append(logDateFormat.format(new Date()));
			myOutWriter.append(toWrite);
            myOutWriter.append("\n====================================================================================\n");
			myOutWriter.close();
			fOut.close();
			
		} catch (IOException ee) {
			ee.printStackTrace();
		}
	}
	
	public static void log(String s) {
		try {
			File externalStorageDir = Environment.getExternalStorageDirectory();
			File myFile = new File(externalStorageDir, "DCIM/hydra.txt");

			if (!myFile.exists()) {
				myFile.createNewFile();
			} 
			
			FileOutputStream fOut = new FileOutputStream(myFile, true);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append("\n====================================================================================\n");
			myOutWriter.append(logDateFormat.format(new Date()));
			myOutWriter.append(s);
            myOutWriter.append("\n====================================================================================\n");
			myOutWriter.close();
			fOut.close();
			
		} catch (IOException ee) {
			ee.printStackTrace();
		}
	}

	public static void logEvent(String event, String message) {
        JSONObject log = new JSONObject();
        try {
            // encrypt message
            SecretKey key = AES256Endec.getInstance().generateKey();
            message = AES256Endec.getInstance().encrypt(message, key);
            
            // prepare event log
            log.put("event", event);
            log.put("message", message);
            String logStr =  log.toString();

            // write event log
            log(logStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
