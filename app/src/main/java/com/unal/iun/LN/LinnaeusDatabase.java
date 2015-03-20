package com.unal.iun.LN;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.unal.iun.MainActivity;
import com.unal.iun.R;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class LinnaeusDatabase extends SQLiteOpenHelper {

	private static String DATABASE_NAME = "datastore.sqlite";
	public final static String DATABASE_PATH = "/data/data/com.unal.iun/databases/";
	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase dataBase;
	private final Context dbContext;

	public LinnaeusDatabase(Context context) {
		super(context, MainActivity.dataBaseName, null, DATABASE_VERSION);
		/*
		 * desde carpetas externas File f=new File(path) if extits f.mkdir();
		 */
		File f = null;
		try{
		if (DATABASE_VERSION == 1) {
			f = new File(DATABASE_PATH + "DataStore.sqlite");
			if (f.exists()) {
				f.delete();
			}
		} else {
			f = new File(DATABASE_PATH + "datastore.sqlite");
			if (f.exists()) {
				f.delete();
			}
		}
		}catch(Exception e){
			Log.e("Base de datos",e.toString());
		}
		this.dbContext = context;
		DATABASE_NAME = MainActivity.dataBaseName;
		// checking database and open it if exists
		if (checkDataBase()) {
			openDataBase();
		} else {
			try {
				this.getReadableDatabase();
				copyDataBase();
				this.close();
				openDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
			Toast.makeText(context, "La Base de datos se ha actualizado",
					Toast.LENGTH_LONG).show();
		}
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = dbContext.getResources().openRawResource(
				R.raw.datastore);
		;// dbContext.getAssets().open(DATABASE_NAME);
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		String dbPath = DATABASE_PATH + DATABASE_NAME;
		dataBase = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		boolean exist = false;
		try {
			String dbPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(dbPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			Log.v("db log", "La Base de Datos no existe");
		}
		if (checkDB != null) {
			exist = true;
			checkDB.close();
		}
		return exist;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}