package com.pghrecycles.pghrecycles.data.providers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLiteOpenHelper for preparing the SQLite database for access; responsible
 * for copying database from apk assets to /data/ .
 * 
 * based on http://stackoverflow.com/questions/513084/how-to-ship-an-android-
 * application-with-a-database
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_PATH = "data/data/com.pghrecycles.pghrecycles/databases/";
	private static final String DATABASE_NAME = "pickup.db";
	private static final int DATABASE_VERSION = 1;
	
	// database we'll be accessing via this helper
	private SQLiteDatabase myDataBase;
	// activity context for this class
	private final Context myContext;
	
	// singleton DataBaseHelper, since we don't need more than 1 of these
	private static DataBaseHelper helper = null;
	
	public static DataBaseHelper getDBHelper(Context context) {
		if (helper == null) {
			helper = new DataBaseHelper(context);
			try {
				helper.createDataBase();
			} catch (IOException e) {
				Log.e(helper.getClass().getName(), "could not open database");
			}
		}
		return helper;
	}
	
	private DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}

	/**
	 * Copies database from APK assets to system. Currently recopying each time.
	 * */
	private void createDataBase() throws IOException {

		// boolean dbExist = checkDataBase();

		// if (dbExist) {
		// // do nothing - database already exist
		// } else {
		// copy database to /data/
		try {
			copyDataBase();
		} catch (IOException e) {
			Log.e(this.getClass().getName(),
					"could not copy database to /data/");
		}

		// this.getReadableDatabase();

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transferring bytestream.
	 * 
	 * Code adopted from http://stackoverflow.com/questions/513084/how-to-ship
	 * -an-android-application-with-a-database
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = DATABASE_PATH + DATABASE_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	private void openDataBase() throws SQLException {

		// Open the database
		String myPath = DATABASE_PATH + DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			createDataBase();
		} catch (IOException e) {
			Log.e(this.getClass().getName(), "could not create / copy database");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO handle database upgrading
	}

}
