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
	private static final String DATABASE_TABLE = "pickup";
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
	
	// information about the global application environment
	private static Context context;
	// private DatabaseOpener dbOpener;
	private DataBaseHelper dbOpener;
	private SQLiteDatabase db;

	public DBPickupInfoProvider(Context context) {
		this.context = context;
		dbOpener = DataBaseHelper.getDBHelper(context);
		db = dbOpener.getReadableDatabase();
	}

//	public void close() {
//		dbOpener.close();
//	}

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
				Log.v(this.getClass().getName(), "more than 1 result returned for query: "+selectStr);
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
				Log.e(this.getClass().getName(), "invalid division name: "+divisionStr);
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
		} finally {
			results.close();
		}

	}


}
