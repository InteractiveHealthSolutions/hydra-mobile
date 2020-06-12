package com.ihsinformatics.dynamicformsgenerator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import android.os.Environment;

public class BackupnRestore {

	public boolean takeBackup(String packageName, String dbName, String destinationPath) {
		try {
		//	File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		    File sd = Environment.getExternalStorageDirectory();
		    File data = Environment.getDataDirectory();

		    if (sd.canWrite()) {
		        String currentDBPath = "//data//"+packageName+"//databases//"+dbName;
		        String backupDBPath = destinationPath+"//"+dbName;
		        File currentDB = new File(data, currentDBPath);
		        File backupDB = new File(sd, backupDBPath);
		        if (!backupDB.exists()) {
		        	backupDB.createNewFile();
				} 
		        if (currentDB.exists()) {
		        	FileInputStream srcInputStream = new FileInputStream(currentDB);
		        	FileOutputStream dstOutputStream = new FileOutputStream(backupDB);		        	
		            FileChannel src = srcInputStream.getChannel();
		            FileChannel dst = dstOutputStream.getChannel();
		            dst.transferFrom(src, 0, src.size());
		            dst.close();
		            src.close();
		            dstOutputStream.close();
		            srcInputStream.close();
		            return true;
		        }
		    }
		} catch (Exception e) {
			Logger.log(e);
		}
		
		return false;
	}
	
	public boolean takeEncryptedBackup(String packageName, String dbName, String destinationPath, String password) {
		try {
	
				File sd = Environment.getExternalStorageDirectory();
				File data = Environment.getDataDirectory();
			
				if (sd.canWrite()) {
				    String currentDBPath = "//data//"+packageName+"//databases//"+dbName;
				    String backupDBPath = destinationPath+"//"+dbName+".zip";
				    File currentDB = new File(data, currentDBPath);
				    File backupDB = new File(sd, backupDBPath);
				    /*if (!backupDB.exists()) { // ??? no need now zipper.pack already handles this
				    	backupDB.createNewFile();
					}*/
				    if (currentDB.exists()) {
				        Zipper zipper = new Zipper();
				        File backupDBZIp = new File(sd, backupDBPath);
				        zipper.pack(currentDB, password, backupDBZIp);
				    }
				    return true;
				}
		    
		} catch (Exception e) {
			Logger.log(e);
		}
		
		return false;
	}
	
	public boolean restore(String packageName, String dbName, String destinationPath) {
		try {
		    File sd = Environment.getExternalStorageDirectory();
		    File data = Environment.getDataDirectory();

		    if (sd.canWrite()) {
		    String currentDBPath = "//data//"+packageName+"//databases//"+dbName;
		        String backupDBPath = destinationPath;
		        File currentDB = new File(data, currentDBPath);
		        File backupDB = new File(sd, backupDBPath);
		        if (!currentDB.exists()) {
		        	currentDB.createNewFile();
				}
		        if (currentDB.exists()) {
		        	FileInputStream srcInputStream = new FileInputStream(backupDB);
		        	FileOutputStream dstOutputStream = new FileOutputStream(currentDB);
		            FileChannel src = srcInputStream.getChannel();
		            FileChannel dst = dstOutputStream.getChannel();
		            dst.transferFrom(src, 0, src.size());
		            dst.close();
		            src.close();
		            dstOutputStream.close();
		            srcInputStream.close();
		            return true;
		        }
		    }
		} catch (Exception e) {
			Logger.log(e);
		}
		
		return false;
	}
}
