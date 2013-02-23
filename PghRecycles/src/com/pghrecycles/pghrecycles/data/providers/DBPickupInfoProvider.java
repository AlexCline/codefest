package com.pghrecycles.pghrecycles.data.providers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.format.Time;
import android.util.Log;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.DivisionInfo.Division;
import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupInfo;

/**
 * Puls PickupInfo from a local DB. (note: code adopted partialy in part from
 * http://stackoverflow.com/questions/5901536/read-database-android-sqlite)
 * 
 * @author alexander.p.conrad@gmail.com
 * 
 */
public class DBPickupInfoProvider implements PickupInfoProvider {

	// constants for interacting with the dbase
	// public static final String KEY_PROJECT = "project";
	// public static final String KEY_NAME = "name";
	// private static final String TAG = "";
	// private static final String DATABASE_PATH =
	// "data/data/com.pghrecycles.pghrecycles/databases/pickup.db";
	private static final String DATABASE_PATH = "data/data/com.pghrecycles.pghrecycles/databases/";
	private static final String DATABASE_NAME = "pickup.db";
	// private static final String DATABASE_NAME = "pickup";
	// private static final String DATABASE_NAME = "/sdcard/pickup.db";
	private static final String DATABASE_TABLE = "pickup";
	private static final int DATABASE_VERSION = 1;
	// private static final String DATABASE_CREATE = "";
	// cols
	private static final String DBASE_COL_STREET = "street";
	private static final String DBASE_COL_LEFT_LOW = "left_low";
	private static final String DBASE_COL_LEFT_HIGH = "left_high";
	private static final String DBASE_COL_RIGHT_LOW = "right_low";
	private static final String DBASE_COL_RIGHT_HIGH = "right_high";
	private static final String DBASE_COL_HOOD = "hood";
	private static final String DBASE_COL_ZIP = "zip";
	private static final String DBASE_COL_DIVISION = "division";
	private static final String DBASE_COL_STREET_BASE = "street_name_base";
	private static final String DBASE_COL_YEAR = "year";
	private static final String DBASE_COL_DAY = "day";
	// "create table project (codProject integer primary key autoincrement, "
	// + "name text);";

	// information about the global application environment
	private static Context context;
	// private DatabaseOpener dbOpener;
	private DataBaseHelper dbOpener;
	private SQLiteDatabase db;

	public DBPickupInfoProvider(Context context) {
		DBPickupInfoProvider.context = context;
		dbOpener = new DataBaseHelper(context);
		try {
			dbOpener.createDataBase();
		} catch (IOException e) {
			Log.e(this.getClass().getName(), "could not open database");
		}
		db = dbOpener.getReadableDatabase();
	}

	public void close() {
		dbOpener.close();
	}

	@Override
	public PickupInfo getPickupInfo(LocationInfo locationInfo, Time yearQuery) {

		int addressNumQuery = -1;
		try {
			addressNumQuery = locationInfo.getAddressNum();
		} catch (NumberFormatException e) {
		}
		int zipQuery = -1;
		try {
			zipQuery = locationInfo.getZip();
		} catch (NumberFormatException e) {
		}
		String streetQuery = locationInfo.getStreet().toUpperCase().trim();
		String streetBaseQuery = locationInfo.getStreet().toUpperCase().trim();

		String selectStr = "SELECT * FROM " + DATABASE_TABLE + " WHERE (("
				+ DBASE_COL_LEFT_LOW + " <= " + addressNumQuery + " AND "
				+ DBASE_COL_LEFT_HIGH + " >= " + addressNumQuery + ") OR ("
				+ DBASE_COL_RIGHT_LOW + " <= " + addressNumQuery + " AND "
				+ DBASE_COL_RIGHT_HIGH + " >= " + addressNumQuery + ") OR ("
				+ DBASE_COL_LEFT_HIGH + " <= " + addressNumQuery + " AND "
				+ DBASE_COL_LEFT_LOW + " >= " + addressNumQuery + ") OR ("
				+ DBASE_COL_RIGHT_HIGH + " <= " + addressNumQuery + " AND "
				+ DBASE_COL_RIGHT_LOW + " >= " + addressNumQuery + ")) AND "
				+ DBASE_COL_ZIP + " = " + zipQuery + " AND ("
				+ DBASE_COL_STREET + " LIKE '" + streetQuery + "%' OR "
				+ DBASE_COL_STREET_BASE + " = '" + streetBaseQuery + "')";
		Log.v(this.getClass().getName(), "query: "+selectStr);
		Cursor results = db.rawQuery(selectStr, null);

		// for now, use 1st result, get relevant fields
		try {
			results.moveToFirst();
			int numResults = results.getCount();
			if (numResults > 1) {
				// TODO more than 1 result; use heuristics to pick most
				// appropriate result?
			}
			int leftLow = results.getInt(results
					.getColumnIndex(DBASE_COL_LEFT_LOW));
			int leftHigh = results.getInt(results
					.getColumnIndex(DBASE_COL_LEFT_HIGH));
			int rightLow = results.getInt(results
					.getColumnIndex(DBASE_COL_RIGHT_LOW));
			int rightHigh = results.getInt(results
					.getColumnIndex(DBASE_COL_RIGHT_HIGH));
			int zip = results.getInt(results.getColumnIndex(DBASE_COL_ZIP));
			String divisionStr = results.getString(results
					.getColumnIndex(DBASE_COL_DIVISION));
			Division division = null;
			if (divisionStr.equalsIgnoreCase(Division.CENTRAL.toString())) {
				division = Division.CENTRAL;
			} else if (divisionStr.equalsIgnoreCase(Division.SOUTHERN
					.toString())) {
				division = Division.SOUTHERN;
			} else if (divisionStr
					.equalsIgnoreCase(Division.EASTERN.toString())) {
				division = Division.EASTERN;
			} else if (divisionStr.equalsIgnoreCase(Division.NORTHERN
					.toString())) {
				division = Division.NORTHERN;
			} else {
				// shouldn't happen, invalid division name
				assert false;
			}
			String streetBase = results.getString(results
					.getColumnIndex(DBASE_COL_STREET_BASE));
			String street = results.getString(results
					.getColumnIndex(DBASE_COL_STREET));
			String hood = results.getString(results
					.getColumnIndex(DBASE_COL_HOOD));
			int year = results.getInt(results.getColumnIndex(DBASE_COL_YEAR));
			String dayStr = results.getString(results
					.getColumnIndex(DBASE_COL_DAY));
			int day = -1;
			if (dayStr.equalsIgnoreCase("sunday")) {
				day = Time.SUNDAY;
			} else if (dayStr.equalsIgnoreCase("monday")
					|| dayStr.equalsIgnoreCase("mon")) {
				day = Time.MONDAY;
			} else if (dayStr.equalsIgnoreCase("tuesday")
					|| dayStr.equalsIgnoreCase("tue")) {
				day = Time.TUESDAY;
			} else if (dayStr.equalsIgnoreCase("wedesday")
					|| dayStr.equalsIgnoreCase("wed")) {
				day = Time.WEDNESDAY;
			} else if (dayStr.equalsIgnoreCase("thursday")
					|| dayStr.equalsIgnoreCase("thu")) {
				day = Time.THURSDAY;
			} else if (dayStr.equalsIgnoreCase("friday")
					|| dayStr.equalsIgnoreCase("fri")) {
				day = Time.FRIDAY;
			} else if (dayStr.equalsIgnoreCase("saturday")) {
				day = Time.SATURDAY;
			} else {
				// invalid day?
				assert false : "invalid day: " + dayStr;
				Log.e(this.getClass().getName(), "invalid day: " + dayStr);
			}

			PickupInfo pickupInfo = new PickupInfo(leftLow, leftHigh, rightLow,
					rightHigh, zip, hood, division, streetBase, street, year,
					day);
			return pickupInfo;
		} catch (CursorIndexOutOfBoundsException e) {
			// no results returned; return empty PickupInfo object
			Log.e(this.getClass().getName(), "no results for LocationInfo: "
					+ locationInfo.toString());
			PickupInfo pickupInfo = new PickupInfo();
			return pickupInfo;
		}

	}

	/**
	 * SQLiteOpenHelper for preparing the SQLite database for access;
	 * responsible for copying database from apk assets to /data/ .
	 * 
	 * based on http://stackoverflow.com/questions/513084/how-to-ship-an-android-application-with-a-database
	 */
	static class DataBaseHelper extends SQLiteOpenHelper {

		// database we'll be accessing via this helper
		private SQLiteDatabase myDataBase;
		// activity context for this class
		private final Context myContext;

		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.myContext = context;
		}

		/**
		 * Copies database from APK assets to system. Currently recopying each
		 * time.
		 * */
		public void createDataBase() throws IOException {

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
		 * Copies your database from your local assets-folder to the just
		 * created empty database in the system folder, from where it can be
		 * accessed and handled. This is done by transferring bytestream.
		 * 
		 * Code adopted from
		 * http://stackoverflow.com/questions/513084/how-to-ship
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

		public void openDataBase() throws SQLException {

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
				Log.e(this.getClass().getName(),
						"could not create / copy database");
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO handle database upgrading
		}

	}

}
