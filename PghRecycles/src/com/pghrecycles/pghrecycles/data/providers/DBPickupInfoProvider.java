package com.pghrecycles.pghrecycles.data.providers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.format.Time;
import android.util.Log;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.DivisionInfo.Division;
import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupInfo;

/**
 * Puls PickupInfo from a local DB.
 * (note: code adopted partialy in part from http://stackoverflow.com/questions/5901536/read-database-android-sqlite)
 * 
 * @author alexander.p.conrad@gmail.com
 *
 */
public class DBPickupInfoProvider implements PickupInfoProvider {
	
	// constants for interacting with the dbase
//	public static final String KEY_PROJECT = "project";
//    public static final String KEY_NAME = "name";
//    private static final String TAG = "";
    private static final String DATABASE_NAME = "pickup";
    private static final String DATABASE_TABLE = "pickup";
    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_CREATE = "";
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
//        "create table project (codProject integer primary key autoincrement, "
//        + "name text);";
	
	
	// information about the global application environment
    private final Context context; 
    private DatabaseOpener dbOpener;
    private SQLiteDatabase db;
    
	public DBPickupInfoProvider(Context context) {
		this.context = context;
		dbOpener = new DatabaseOpener(context);
		db = dbOpener.getReadableDatabase();
		
	}
	
//	public DBPickupInfoProvider open() throws SQLException {
//        db = dbOpener.getWritableDatabase();
//        return this;
//    }
//
    public void close() {
        dbOpener.close();
    }
	
	@Override
	public PickupInfo getPickupInfo(LocationInfo locationInfo, Time yearQuery) {
		
		int addressNumQuery = locationInfo.getAddressNum();
		int zipQuery = locationInfo.getZip();
		String streetQuery = locationInfo.getStreet();
//		String streetBase = locationInfo.getStreetBase();
		
		String selectStr = "SELECT * FROM "+DATABASE_TABLE+" WHERE '"+DBASE_COL_LEFT_LOW+"' <= "+addressNumQuery+" AND '"+DBASE_COL_LEFT_HIGH+"' >= "+addressNumQuery+" OR '"+DBASE_COL_RIGHT_LOW+"' <= "+addressNumQuery+" AND '"+DBASE_COL_RIGHT_HIGH+"' >= "+addressNumQuery+" AND '"+DBASE_COL_YEAR+"' = "+yearQuery.year;
		Cursor results = db.rawQuery(selectStr, null);
		
		// TODO if we have multiple results, choose the row for which oddness or evenness matches the left_low or right_low
		// TODO proper try / catch handling (for missing results)
		
		// for now, use 1st result, get relevant fields
		results.moveToFirst();
		int leftLow = results.getInt(results.getColumnIndex(DBASE_COL_LEFT_LOW));
		int leftHigh = results.getInt(results.getColumnIndex(DBASE_COL_LEFT_HIGH));
		int rightLow = results.getInt(results.getColumnIndex(DBASE_COL_RIGHT_LOW));
		int rightHigh = results.getInt(results.getColumnIndex(DBASE_COL_RIGHT_HIGH));
		int zip = results.getInt(results.getColumnIndex(DBASE_COL_ZIP));
		String divisionStr = results.getString(results.getColumnIndex(DBASE_COL_DIVISION));
		Division division = null;
		if (divisionStr.equalsIgnoreCase(Division.CENTRAL.toString())) {
			division = Division.CENTRAL;
		} else if (divisionStr.equalsIgnoreCase(Division.SOUTHERN.toString())) {
			division = Division.SOUTHERN;
		} else if (divisionStr.equalsIgnoreCase(Division.EASTERN.toString())) {
			division = Division.EASTERN;
		} else if (divisionStr.equalsIgnoreCase(Division.NORTHERN.toString())) {
			division = Division.NORTHERN;
		} else {
			// shouldn't happen, invalid division name
			assert false;
		}
		String streetBase = results.getString(results.getColumnIndex(DBASE_COL_STREET_BASE));
		String street = results.getString(results.getColumnIndex(DBASE_COL_STREET));
		String hood = results.getString(results.getColumnIndex(DBASE_COL_HOOD));
		int year = results.getInt(results.getColumnIndex(DBASE_COL_YEAR));
		int day = results.getInt(results.getColumnIndex(DBASE_COL_DAY));
		
		PickupInfo pickupInfo = new PickupInfo(leftLow, leftHigh, rightLow, rightHigh, zip, hood, division, streetBase, street, year, day);
		return pickupInfo;
		
	}
	
	/**
	 * subclass of SQLiteOpenHelper, purpose is to provide access to back-end dbase
	 * 
	 * @author alexander.p.conrad@gmail.com
	 *
	 */
	private static class DatabaseOpener extends SQLiteOpenHelper {
		
        DatabaseOpener(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	// we shouldn't be calling this method; assert: database already exists!
//            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	// we shouldn't be calling this method; database should not be modifiable by application?
//            Log.w(TAG, "Upgrading database from version " + oldVersion 
//                    + " to "
//                    + newVersion + ", which will destroy all old data");
//            db.execSQL("DROP TABLE IF EXISTS projecto");
//            onCreate(db);
        }
        
//        @Override
//        public void onOpen(SQLiteDatabase db) {
//        	
//        }
        
    } 
	
}
