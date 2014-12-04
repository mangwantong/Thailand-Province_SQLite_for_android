package com.ikainui.easyquest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static String DB_PATH = "/data/data/com.android.csvParsing/databases/";
	public static String DB_NAME = "mydb";
	public static final int DB_VERSION = 2;
	public String name_onselect="";
	public static final String TB_USER = "location";
	
	private SQLiteDatabase myDB;
	//private Context context;
	private Context context = getApplicationContext();
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);	
		this.context = context;
	}

	private Context getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void close(){
		if(myDB!=null){
			myDB.close();
		}
		super.close();
	}
		
	public List<String> getAllUsers(){
		List<String> listUsers = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c;
		
		try {
			c = db.rawQuery("SELECT DISTINCT province FROM " + TB_USER +" order by province ASC", null);
			if(c == null) return null;
			
			String name;
			c.moveToFirst();
			do {			
				name = c.getString(0);			
				listUsers.add(name);
			} while (c.moveToNext()); 
			c.close();
		} catch (Exception e) {
			Log.e("tle99", e.getMessage());
		}
		
		
		db.close();		
		
		return listUsers;
	}
	
	
	public List<String> select_district(String var_sel){
		List<String> listdistrict = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c;
		try {
			c = db.rawQuery("SELECT DISTINCT district FROM " + TB_USER +" where province = '"+var_sel+"'  order by district ASC", null);
			if(c == null) return null;
			
			String name;
			c.moveToFirst();
			do {			
				name = c.getString(0);			
				listdistrict.add(name);
			} while (c.moveToNext()); 
			c.close();
		} catch (Exception e) {
			Log.e("tle99", e.getMessage());
		}
		
		
		db.close();	
		//String test_var = listdistrict.get(0);
		//Log.e("LOG", test_var);
		return listdistrict;
		
	}
	
	public String select_zipcode(String var_sel_p,String var_sel_d){
		String txt_zipcord ="";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c;
		try {
			c = db.rawQuery("SELECT DISTINCT zip FROM " + TB_USER +" where province = '"+var_sel_p+"' and district ='"+var_sel_d+"'  ", null);
			if(c == null) return null;
			
			String name;
			c.moveToFirst();
			do {			
				name = c.getString(0);			
				txt_zipcord =name;
			} while (c.moveToNext()); 
			c.close();
		} catch (Exception e) {
			Log.e("tle99", e.getMessage());
		}
		
		
		db.close();	
		//String test_var = listdistrict.get(0);
		//Log.e("LOG", test_var);
		return txt_zipcord;
		
	}
	
	public List<String> select_subdistrict(String var_sel_p,String var_sel_d){
		List<String> listsubdistrict = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c;
		try {
			c = db.rawQuery("SELECT DISTINCT subdistrict FROM " + TB_USER +" where province = '"+var_sel_p+"' and district ='"+var_sel_d+"'  order by subdistrict ASC", null);
			if(c == null) return null;
			
			String name;
			c.moveToFirst();
			do {			
				name = c.getString(0);			
				listsubdistrict.add(name);
			} while (c.moveToNext()); 
			c.close();
		} catch (Exception e) {
			Log.e("tle99", e.getMessage());
		}
		
		
		db.close();	
		//String test_var = listdistrict.get(0);
		//Log.e("LOG", test_var);
		return listsubdistrict;
		
	}
	
	/***
	 * Open database
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException{
		String myPath = DB_PATH + DB_NAME;
		myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}

	/***
	 * Copy database from source code assets to device
	 * @throws IOException
	 */
	public void copyDataBase() throws IOException{
		try {
			InputStream myInput = context.getAssets().open(DB_NAME);
			String outputFileName = DB_PATH + DB_NAME;
			OutputStream myOutput = new FileOutputStream(outputFileName);

			byte[] buffer = new byte[1024];
			int length;

			while((length = myInput.read(buffer))>0){
				myOutput.write(buffer, 0, length);
			}

			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {
			Log.e("tle99 - copyDatabase", e.getMessage());
		}
		
	}
	
	/***
	 * Check if the database doesn't exist on device, create new one
	 * @throws IOException
	 */
//	public void createDataBase() throws IOException {
//		boolean dbExist = checkDataBase();		
//		
//		if (dbExist) {
//
//		} else {
//			this.getReadableDatabase();
//			try {
//				copyDataBase();
//			} catch (IOException e) {
//				Log.e("tle99 - create", e.getMessage());
//			}
//		}
//	}
	
	
	public void createDataBase() throws IOException{ 

		boolean dbExist = checkDataBase();
		SQLiteDatabase db_Read = null;

		if(dbExist){ 
		//do nothing - database already exist 
		}else{ 

		//By calling this method and empty database will be created into the default system path 
		//of your application so we are gonna be able to overwrite that database with our database. 
		db_Read = this.getReadableDatabase(); 
		db_Read.close();

		try { 

		copyDataBase(); 

		} catch (IOException e) { 

		throw new Error("Error copying database"); 

		} 
		} 

		} 
	
	
	// ---------------------------------------------
	// PRIVATE METHODS
	// ---------------------------------------------
	
	/***
	 * Check if the database is exist on device or not
	 * @return
	 */
	private boolean checkDataBase() {
		SQLiteDatabase tempDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			tempDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			Log.e("tle99 - check", e.getMessage());
		}
		if (tempDB != null)
			tempDB.close();
		return tempDB != null ? true : false;
	}
	

}
